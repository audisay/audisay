# 컨트롤러와 path 연결해주는 파일같음

from django.urls import path
from .views import OcrProcessingView, PdfProcessingView

urlpatterns = [
    path('ocr/', OcrProcessingView.as_view(), name='ocr_process'),
    path('pdf/', PdfProcessingView.as_view(), name='pdf_process')
]