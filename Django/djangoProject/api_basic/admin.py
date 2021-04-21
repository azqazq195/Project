from django.contrib import admin
from .models import Article
from .product.models import Product

# Register your models here.

admin.site.register(Article)
admin.site.register(Product)
