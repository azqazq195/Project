import jwt
import requests
import json
import bcrypt
from rest_framework import viewsets, status
from rest_framework.response import Response
from .models import User
from .serializers import UserSerializer
from rest_framework.views import APIView
from djangoProject.settings import SECRET_KEY
from django.http import JsonResponse


# Create your views here.
class UserViewSet(viewsets.ModelViewSet):
    serializer_class = UserSerializer
    queryset = User.objects.all()


class SignUp(APIView):
    def post(self, request):
        data = json.loads(request.body)
        print(data)
        try:
            if User.objects.filter(email=data['email']).exists():
                return Response({"message": "EXISTS_EMAIL"}, status.HTTP_400_BAD_REQUEST)

            name = data['name']
            email = data['email']
            password = bcrypt.hashpw(data['password'].encode('UTF-8'), bcrypt.gensalt()).decode('UTF-8')
            User.objects.create(
                name=name,
                email=email,
                password=password
            ).save()
            return Response({"message": "Create User"}, status.HTTP_200_OK)
        except Exception as ex:
            return Response(ex, status.HTTP_400_BAD_REQUEST)
    # if serializer.is_valid():
    #     return Response(serializer.data, status=status.HTTP_201_CREATED)
    # else:
    #     return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class SignIn(APIView):
    def post(self, request):
        data = json.loads(request.body)
        try:
            if User.objects.filter(email=data['email']).exists():
                user = User.objects.get(email=data['email'])

                if bcrypt.checkpw(data['password'].encode('UTF-8'), user.password.encode('UTF-8')):
                    token = jwt.encode({"token": user.id}, SECRET_KEY, algorithm='HS256').encode('UTF-8')
                    response = {
                        "message": "Sign In Complete",
                        "user": {
                            'token': token,
                            'name': user.name,
                            'email': user.email,
                            'password': user.password
                        }
                    }

                    return Response(response, status=status.HTTP_200_OK)
                    # token = jwt.encode(UserSerializer(user).data, SECRET_KEY, algorithm='HS256').encode('UTF-8')
                    # return Response({"token": token}, status=status.HTTP_200_OK)
                else:
                    return Response({"message": "Wrong Password"}, status=status.HTTP_401_UNAUTHORIZED)
            else:
                return Response({"message": "Cannot Find Email"}, status=status.HTTP_400_BAD_REQUEST)
        except Exception as ex:
            return Response(ex, status.HTTP_400_BAD_REQUEST)
