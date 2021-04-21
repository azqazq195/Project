import jwt
import requests
import json
import bcrypt
from rest_framework import viewsets, status
from rest_framework.response import Response
from .models import Order
from .serializers import OrderSerializer
from rest_framework.views import APIView
from djangoProject.settings import SECRET_KEY
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated
from django.http import JsonResponse
from django.core import serializers


# Create your views here.
class OrderViewSet(viewsets.ModelViewSet):
    serializer_class = OrderSerializer
    queryset = Order.objects.all()
    authentication_classes = [TokenAuthentication]
    permission_classes = [IsAuthenticated]


class OrderAPI(APIView):
    def get(self, request):
        data = request.query_params.get('token')
        decoded = jwt.decode(data, SECRET_KEY, algorithms='HS256')
        orders = Order.objects.all().filter(userId=decoded['token'])
        serializer = OrderSerializer(orders, many=True)
        return Response({"orders": serializer.data}, status.HTTP_200_OK)

    def post(self, request):
        data = json.loads(request.body)
        decoded = jwt.decode(data['userId'], SECRET_KEY, algorithms='HS256')
        Order.objects.create(
            userId=decoded.get('token'),
            productId=data['productId'],
            status="결제 완료"
        ).save()
        return Response({"message": "Order Done"}, status.HTTP_200_OK)
