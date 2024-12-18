from .base import *

ALLOWED_HOSTS = ['5f3f-211-194-200-134.ngrok-free.app', 'localhost']

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': env('MYSQL_SCHEMA_NAME'), # db 이름
        'USER': env('MYSQL_ROOT_USERNAME'), # 유저 이름
        'PASSWORD': env('MYSQL_ROOT_PASSWORD'),
        'HOST': 'audisay.kr', # host 이름 k11d208.p.ssafy.io
        'PORT': '36284'
    }
}

# fastapi url
FASTAPI_URL = "http://localhost:5000"

DATABASE_MONGO = env('MONGO_DB_URI')

# redis 설정
CACHES = {
    "default": {
        "BACKEND": "django_redis.cache.RedisCache",
        "LOCATION": f"redis://{env('SERVER_NAME')}:{env('REDIS_BINDING_PORT')}/0",
        "OPTIONS": {
            "CLIENT_CLASS": "django_redis.client.DefaultClient",
            "PASSWORD": env('REDIS_PASSWORD')
        }
    }
}