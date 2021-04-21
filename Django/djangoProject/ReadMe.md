# Django

## Model
- User
- Product
- Order

ex) User
```python
# Create your models here.
class Order(models.Model):
    userId = models.IntegerField()
    productId = models.CharField(max_length=120)
    orderDate = models.DateTimeField(auto_now_add=True)
    status = models.CharField(max_length=120)

    def __str__(self):
        return self.productId
```

## View
각 모델별 전체 데이터 조회, 수정, 삭제 권한을 가진 관리자 ViewSet이 존재

![viewset List](https://user-images.githubusercontent.com/45132207/115148727-de9ae400-a09b-11eb-8b55-e9d00cc0eeef.PNG)

해당 경로는 권한 필요
```request
-H Authorization Token e71b9d323e5033ef2193ff466024436591bac210
```

![관리자_비로그인](https://user-images.githubusercontent.com/45132207/115148764-0f7b1900-a09c-11eb-988b-7f129fdce7de.PNG)

![관리자_로그인](https://user-images.githubusercontent.com/45132207/115148767-130ea000-a09c-11eb-81b2-756626a8027a.PNG)

### User

User 비밀번호 암호화 => bcrypt 라이브러리

![viewset user list](https://user-images.githubusercontent.com/45132207/115148814-46512f00-a09c-11eb-903e-ab48b25402f0.PNG)

User 정보 확인시 토큰 활용
```python
encode = jwt.encode({"token": user.id}, SECRET_KEY, algorithm='HS256').encode('UTF-8')

decoded = jwt.decode(data, SECRET_KEY, algorithms='HS256')
```

### Product

상품 정보들은 임시 제작을 위해 네이버 검색 API를 이용하여 sqlite에 저장

```python
    def get(self, request):
        response = requests.get(
            'https://openapi.naver.com/v1/search/shop.json?query=셔츠&display=10&start=1&sort=sim',
            headers={
                "X-Naver-Client-Id": NAVER_ID,
                "X-Naver-Client-Secret": NAVER_SECERT
            }
        )
        data = json.loads(response.content.decode("utf-8"))['items']
```
![products](https://user-images.githubusercontent.com/45132207/115148820-4a7d4c80-a09c-11eb-866e-a2767f637802.PNG)

### Order
사용자가 주문한 정보 가져오기.
Token화 시킨 유저 아이디 값으로 검색
![order](https://user-images.githubusercontent.com/45132207/115148998-f7f06000-a09c-11eb-93e0-6dc7f67f8a46.PNG)

### urlpatterns
URL
```python
urlpatterns = [
    path('viewset/', include(router.urls)),
    path('viewset/<int:pk>', include(router.urls)),
    path('user/signin/', SignIn.as_view()),
    path('user/signup/', SignUp.as_view()),
    path('product/', ProductAPIView.as_view()),
    path('order/', OrderAPI.as_view())
]
```
