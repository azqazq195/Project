from django.db import models
from api_basic.product.models import Product


# Create your models here.
class Order(models.Model):
    userId = models.IntegerField()
    productId = models.CharField(max_length=120)
    orderDate = models.DateTimeField(auto_now_add=True)
    status = models.CharField(max_length=120)

    def __str__(self):
        return self.productId

