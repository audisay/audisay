import logging
import boto3
from botocore.exceptions import ClientError
from config.settings.base import AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_S3_BUCKET, AWS_REGION
from ebooklib import epub
import io 
from datetime import datetime
import numpy as np
from PIL import Image
from urllib.parse import quote

class S3Client:

    def upload_fileobj(self, file_object, s3_key):
        """
        S3 버켓에 파일 객체(바이너리) 업로드. 
        file_object: 업로드할 파일 객체
        bucket: 버킷 이름
        s3_key: S3에 저장할 경로 (파일명까지 포함)
        return: boolean
        """
        client = boto3.client(  # 정확한 타입 힌트 추가
            's3',
            aws_access_key_id=AWS_ACCESS_KEY,
            aws_secret_access_key=AWS_SECRET_KEY,
            region_name=AWS_REGION
        )

        try:
            response = client.upload_fileobj(file_object, AWS_S3_BUCKET, s3_key)
            logging.info(response)
        except ClientError as e:
            logging.error(e)
            return False
        return True
    
    def generate_download_url(self, s3_key, expiration=3600):
        """
        다운로드(presigned) URL 생성
        s3_key: 다운로드할 파일의 키 (경로)
        expiration: URL 유효 시간 (초단위, 기본값 3600초)
        return: 다운로드 url
        """
        client = boto3.client(  # 정확한 타입 힌트 추가
            's3',
            aws_access_key_id=AWS_ACCESS_KEY,
            aws_secret_access_key=AWS_SECRET_KEY,
            region_name=AWS_REGION
        )
        try:
            url = client.generate_presigned_url(
                'get_object',
                Params={
                    'Bucket': AWS_S3_BUCKET,
                    'Key': s3_key
                },
                ExpiresIn=expiration
            )
            return url
        except Exception as e:
            logging.error(e)
            return None
    
    def upload_epub_to_s3(self, book: epub, filename: str, metadata: object, member_id: int, test=False):
        """"""
        buffer = io.BytesIO()
        try:
            if test:
                test_s3_key = 'epub/registered/1/narnia.epub'
                test_download_url = self.generate_download_url(s3_key=test_s3_key)
                return {
                    "epub": test_download_url,
                    "dtype": "REGISTERED",
                    "metadata": {
                        "title": metadata.get('title', '(제목 미정)'),
                        "author": metadata.get('author', '(작자 미상)'),
                        "created_at": metadata.get('created_at', datetime.now().isoformat()),
                        "cover": metadata.get('cover') 
                    }
                }
            
            epub.write_epub(buffer, book) # 버퍼에 epub 저장
            buffer.seek(0)
            s3_key = f'epub/registered/{member_id}/{filename}' # S3 내 저장 경로
            self.upload_fileobj(file_object=buffer, s3_key=s3_key) # 파일 업로드
            download_url = self.generate_download_url(s3_key=s3_key) # 다운로드 링크 생성

            return {
                "epub": download_url,
                "dtype": "REGISTERED",
                "metadata": {
                    "title": metadata.get('title', '(제목 미정)'),
                    "author": metadata.get('author', '(작자 미상)'),
                    "created_at": metadata.get('created_at', datetime.now().isoformat()),
                    "cover": metadata.get('cover')
                }
            }
        except Exception as e:
            print(f'EPUB 파일 생성 중 에러 발생: {str(e)}')
            return None
            
    def convert_to_rgb_jpeg(self, pil_image):
        """
        RGBA 이미지를 RGB JPEG 형식으로 변환
        """
        if pil_image.mode == 'RGBA':
            background = Image.new('RGB', pil_image.size, (255, 255, 255))
            background.paste(pil_image, mask=pil_image.split()[3])
            return background
        return pil_image
    
    def detect_format(self, array: np.ndarray) -> str:
        """
        numpy array 이미지 형식 감지
        """
        if len(array.shape) == 3 and array.shape[2] == 4:
            return 'PNG'
        return 'JPEG'
            
    def save_numpy_to_s3(self, array: np.ndarray, s3_key: str) -> str:
        """이미지를 jpg로 저장하고 url 반환"""
        client = boto3.client(  # 정확한 타입 힌트 추가
            's3',
            aws_access_key_id=AWS_ACCESS_KEY,
            aws_secret_access_key=AWS_SECRET_KEY,
            region_name=AWS_REGION
        )

        # 이미지 형식 감지
        image_format = self.detect_format(array)
        pil_cover = Image.fromarray(array)
        buffer = io.BytesIO()

        if image_format == 'PNG':
            pil_cover.save(buffer, format='PNG')
            content_type = 'image/png'
            if not s3_key.lower().endswith('.png'):
                s3_key = s3_key.rsplit('.', 1)[0] + '.png'
        else:
            pil_cover = self.convert_to_rgb_jpeg(pil_cover)
            pil_cover.save(buffer, format='JPEG')
            content_type = 'image/jpeg'
            if not s3_key.lower().endswith(('.jpg', '.jpeg')):
                s3_key = s3_key.rsplit('.', 1)[0] + '.jpg'

        buffer.seek(0)

        extra_args = {
            'ContentType': content_type,
            'ContentDisposition': 'inline'
        }

        client.upload_fileobj(buffer, AWS_S3_BUCKET, s3_key, ExtraArgs=extra_args)

        url = f'https://{AWS_S3_BUCKET}.s3.{AWS_REGION}.amazonaws.com/{quote(s3_key)}'
        return url
