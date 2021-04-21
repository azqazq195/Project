# 장고 프로젝트

## 튜토리얼

### 초기 설정
```shell
pip install rest_framework
pip install django-filter

python manage.py runserver
python manage.py startapp api_basic
python manage.py migrate

// 관리자 계정 만들기
python manage.py createsuperuser
```

### 관리자 계정에서 모델 관리

admin.py 에 등록

```python
from django.contrib import admin
from .models import Article

# Register your models here.

admin.site.register(Article)
```

### 모델 만들기

models.py 에서 class 작성 후 마이그레이션
```shell
python manage.py makemigrations
python manage.py migrate
```

### View 만들기

Article 에 등록된 데이터를 JSON 형식으로 내보내 준다.

```python
from .models import Article
from .serializers import ArticleSerializer
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status


# Create your views here.

@api_view(['GET', 'POST'])
def article_list(request):
    if request.method == 'GET':
        articles = Article.objects.all()
        serializer = ArticleSerializer(articles, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)

    if request.method == 'POST':
        serializer = ArticleSerializer(data=request.data)

        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
```

### URL 등록

프로젝트 폴더의 url 에서 api 폴더의 url 을 등록해준다.

프로젝트
```python
from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', include('api_basic.urls'))
]
```

api 폴더
```python
from django.urls import path, include
from .views import article_list

urlpatterns = [
    path('article/', article_list),
]
```
