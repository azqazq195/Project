# Android Studio

## 내 정보 화면

### 비로그인시 화면

![안드 내정보_비로그인](https://user-images.githubusercontent.com/45132207/115149106-89f86880-a09d-11eb-8806-8423f5c232e0.png)

로그인 혹은 회원가입 가능

---


### 회원가입 화면

![안드 회원가입](https://user-images.githubusercontent.com/45132207/115149108-8b299580-a09d-11eb-8e54-91222d1ea55b.png)

회원가입 가능
Email 중복 불가 (python 서버 설정)

---

### 로그인 화면

![안드 로그인](https://user-images.githubusercontent.com/45132207/115149109-8c5ac280-a09d-11eb-9e16-c50ec38a1035.png)

로그인 완료 후 아이디값을 토큰으로 저장, 자동 로그인

### 로그인 완료시 화면

![안드 내정보_로그인](https://user-images.githubusercontent.com/45132207/115149111-8cf35900-a09d-11eb-90dd-9b9d2b556f38.png)

자동 로그인 상태
로그아웃 시 아이디값 삭제

### 주문 내역

![안드 주문내역](https://user-images.githubusercontent.com/45132207/115149112-8e248600-a09d-11eb-92a0-e208239bded1.png)

주문 내역 클릭시 해당 사용자의 Order 정보 불러오기

## 홈 화면

### 상품 화면

![안드_상품](https://user-images.githubusercontent.com/45132207/115149164-c9bf5000-a09d-11eb-850c-352a15cadc5b.PNG)

네이버 검색 API를 통해 저장해 놓은 상품들을 불러오기

### 상품 디테일 화면

![안드_상품디테일](https://user-images.githubusercontent.com/45132207/115149166-caf07d00-a09d-11eb-85ad-8021dfa64f64.PNG)

클릭시 상품화면으로 넘어가고, 구매가 가능

---

사용 라이브러리
```gradle
//noinspection GradleDependency
implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
implementation 'androidx.core:core-ktx:1.3.2'
implementation 'androidx.appcompat:appcompat:1.2.0'
implementation 'com.google.android.material:material:1.3.0'
implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
testImplementation 'junit:junit:4.13.2'
androidTestImplementation 'androidx.test.ext:junit:1.1.2'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'

def lifecycle_version = "2.3.1"
implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

def nav_version = "2.3.5"
// Kotlin
implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

// Feature module Support
implementation "androidx.navigation:navigation-dynamic-features-fragment:$nav_version"

// Testing Navigation
androidTestImplementation "androidx.navigation:navigation-testing:$nav_version"

// Jetpack Compose Integration
implementation "androidx.navigation:navigation-compose:1.0.0-alpha10"

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3'

// Retrofit
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation "com.squareup.okhttp3:logging-interceptor:4.5.0"

// jwt
implementation 'io.jsonwebtoken:jjwt:0.7.0'

// Glide
implementation 'com.github.bumptech.glide:glide:4.11.0'
kapt 'com.github.bumptech.glide:compiler:4.11.0'
```
