from django.db import models


# Create your models here.

class Product(models.Model):
    title = models.CharField(max_length=120)
    link = models.CharField(max_length=120)
    image = models.CharField(max_length=120)
    lprice = models.CharField(max_length=120)
    hprice = models.CharField(max_length=120, blank=True, null=True)
    mallName = models.CharField(max_length=120, blank=True, null=True)
    productId = models.CharField(max_length=120, unique=True)
    productType = models.CharField(max_length=120)
    brand = models.CharField(max_length=120, blank=True, null=True)
    maker = models.CharField(max_length=120, blank=True, null=True)
    category1 = models.CharField(max_length=120, blank=True, null=True)
    category2 = models.CharField(max_length=120, blank=True, null=True)
    category3 = models.CharField(max_length=120, blank=True, null=True)
    category4 = models.CharField(max_length=120, blank=True, null=True)

    def __str__(self):
        return self.title
