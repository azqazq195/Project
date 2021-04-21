from rest_framework import viewsets, status
from rest_framework.response import Response
from .models import Product
from .serializers import ProductSerializer
from rest_framework.views import APIView
import requests, json


# Create your views here.


class ProductViewSet(viewsets.ModelViewSet):
    serializer_class = ProductSerializer
    queryset = Product.objects.all()

    def get(self, request):
        response = requests.head(
            'https://openapi.naver.com/v1/search/shop.json?query=셔츠&display=10&start=1&sort=sim',
            params={
                "X-Naver-Client-Id": "aB2mSrVOh8CYymmT3RD3",
                "X-Naver-Client-Secret": "4rCYJR3mtS"
            }
        )
        data = request.data
        print(data)
        return data


class ProductAPIView(APIView):

    def get(self, request):
        response = requests.get(
            'https://openapi.naver.com/v1/search/shop.json?query=셔츠&display=10&start=1&sort=sim',
            headers={
                "X-Naver-Client-Id": "aB2mSrVOh8CYymmT3RD3",
                "X-Naver-Client-Secret": "4rCYJR3mtS"
            }
        )
        data = json.loads(response.content.decode("utf-8"))['items']
        return Response({"products": data}, status=status.HTTP_200_OK)
