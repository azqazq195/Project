from django.urls import path, include
# from .views import article_list, article_details
from .views import ArticleAPIView, ArticleDetailsAPIView, GenericAPIView, ArticleViewSet
from .product.views import ProductViewSet, ProductAPIView
from .user.views import UserViewSet, SignIn, SignUp
from .order.views import OrderViewSet, OrderAPI

from rest_framework.routers import DefaultRouter

router = DefaultRouter()
router.register('article', ArticleViewSet, basename='article')
router.register('product', ProductViewSet, basename='product')
router.register('user', UserViewSet, basename='user')
router.register('order', OrderViewSet, basename='order')

urlpatterns = [
    path('viewset/', include(router.urls)),
    path('viewset/<int:pk>', include(router.urls)),
    path('article/', ArticleAPIView.as_view()),
    path('product/', ProductAPIView.as_view()),
    path('user/signin/', SignIn.as_view()),
    path('user/signup/', SignUp.as_view()),
    path('order/', OrderAPI.as_view()),
    path('article/details/<int:id>', ArticleDetailsAPIView.as_view()),
    path('generic/article/<int:id>', GenericAPIView.as_view()),
]

