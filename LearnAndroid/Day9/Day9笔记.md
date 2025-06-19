### Day9 笔记



#### Android权限

```
<user-permission android:name=""/>
```



##### 普通权限

- 正常权限，风险不大
- 敏感权限，容易泄露隐私



**TIPS:**

1. 敏感权限根据功能划分成了不同权限组
2. 任何权限都要在manifest中申请
3. Android6.0以下安装时申请权限、以上运行时动态申请权限



##### Android6.0以上申请步骤

1. 检查是否授权 **ContexCompat.checkSelfPermission**
2. 权限申请         **requestPermission**
3. 权限申请结果  **onRequestPermissionsResult**



**Question** : 如果申请权限被永久拒绝怎么办？

​	如果用户在 Android 6.0 及以上版本中选择了“拒绝并不再询问”选项，那么应用将无法再次弹出权限请求对话框。此时，应用需要引导用户手动到系统设置中为应用授予所需的权限。可以通过以下步骤进行处理：

1. **检查权限状态**：在需要使用权限的地方，首先检查权限是否已经被授予。
2. **显示解释说明**：如果权限被永久拒绝（`shouldShowRequestPermissionRationale` 返回 `false`），可以显示一个对话框或提示，引导用户到设置中手动开启权限。
3. **打开设置页面**：通过 Intent 打开应用的系统设置页面，用户可以在其中手动授予权限。



#### Android的HTTP请求



##### HTTP请求类型

- GET
- POST
- HEAD
- PUT
  DELETE
- CONNECT
- OPTIONS
- TRACE
- PATCH

###### Get 请求

1. 从服务端获取数据
2. 使用 ? 添加在Uri后面，k=v 格式参数，多个参数使用 & 相连
3. 提交数据最多1024字节
4. 如果get请求的是静态资源，则会缓存

##### Post请求

1. post请求向服务端写入数据
2. 更安全，参数放在request body中传递

###### Http请求状态码

1XX	正在处理

2XX	正常处理完毕

3XX	需要附加操作以完成请求

4XX	服务器无法处理请求

5XX	服务器处理请求出错



##### HTTP请求API变迁

- ###### HttpURLConnection 

- ###### HttpClient

- ###### Volley

- ###### OkHttp



发起请求

开启子线程

请求服务

获取结果

- 主线程更新UI
- 当前线程更新数据
- 切换任务线程与其他任务协同



##### OKHTTP

###### get 同步请求

1. 创建OkHttpClient 
2. 创建Request
3. 同步请求

```java
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class OkHttpSyncGetExample {
    public static void main(String[] args) {
        // 1. 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();

        // 2. 创建 Request 实例
        Request request = new Request.Builder()
                .url("https://www.example.com")
                .build();

        // 3. 发起同步请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                System.out.println(responseData);
            } else {
                System.out.println("Request failed: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```

###### 异步 GET 请求

1. 创建 OkHttpClient 实例
2. 创建 Request 实例
3. 发起异步请求并处理响应

```java
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class OkHttpAsyncGetExample {
    public static void main(String[] args) {
        // 1. 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();

        // 2. 创建 Request 实例
        Request request = new Request.Builder()
                .url("https://www.example.com")
                .build();

        // 3. 发起异步请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    System.out.println(responseData);
                } else {
                    System.out.println("Request failed: " + response.code());
                }
            }
        });

        // 为了演示的目的，保持主线程运行
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```

同步execute，异步enqueue



###### GSON 

json解析库

添加依赖

```
implementation ("com.google.code.gson:gson:2.8.9")
```

**1、序列化**

```java
import com.google.gson.Gson;

// 创建一个 Java 对象
Person person = new Person("John", 30);

// 创建 Gson 对象
Gson gson = new Gson();

// 将 Java 对象转换为 JSON 字符串
String jsonString = gson.toJson(person);
System.out.println(jsonString);

```

**2、反序列化**

```java
import com.google.gson.Gson;

// JSON 字符串
String jsonString = "{\"name\":\"John\",\"age\":30}";

// 创建 Gson 对象
Gson gson = new Gson();

// 将 JSON 字符串转换为 Java 对象
Person person = gson.fromJson(jsonString, Person.class);
System.out.println("Name: " + person.getName());
System.out.println("Age: " + person.getAge());

```

**3、复杂json解析**

```
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

// JSON 字符串
String jsonString = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]";

// 创建 Gson 对象
Gson gson = new Gson();

// 使用 TypeToken 解析 JSON 数组
Type personListType = new TypeToken<List<Person>>() {}.getType();
List<Person> personList = gson.fromJson(jsonString, personListType);

for (Person person : personList) {
    System.out.println("Name: " + person.getName());
    System.out.println("Age: " + person.getAge());
}

```



###### Request构建

`MultipartBody.Builder()`自定义数据构造请求体

post需要请求体，可以使用MultipartBody上传文件、视频等类型数据

还可以使用FormBody来上传键值对类型的表单数据



###### 拦截器

应用场景

- **日志记录**：记录请求和响应的详细信息，方便调试和排查问题。
- **请求重试**：在网络请求失败时自动重试。
- **认证**：添加认证信息到每个请求中。
- **修改请求头和响应头**：添加、删除或修改请求头和响应头。
- **数据压缩**：在请求和响应时压缩数据。
- **缓存**：检查缓存并返回缓存的响应。
- **请求重定向**：拦截并重定向请求到其他URL。

拦截器链

​	在 `OkHttpClient` 中，可以添加多个拦截器。它们会按照添加的顺序依次执行，形成一个拦截器链。每个拦截器都可以对请求和响应进行处理，并将处理结果传递给下一个拦截器，或者直接处理请求和响应。

使用方法：

1、` implements Interceptor`创建拦截器

2、给请求添加拦截器



###### OkHttp 组成部分

1. **OkHttpClient**:
   - `OkHttpClient` 是 OkHttp 的核心管理类，用于创建和管理网络请求。
   - 它负责建立连接、设置超时、添加拦截器等。
   - 通过 `OkHttpClient` 发送请求并接收响应。
2. **Request**:
   - `Request` 类表示一个 HTTP 请求。
   - 它包含了请求的 URL、请求方法（GET、POST 等）、请求头、请求体等信息。
   - 通过 `Request` 发送需要的数据到服务器。
3. **Response**:
   - `Response` 类表示一个 HTTP 响应。
   - 它包含了响应的状态码、响应头、响应体等信息。
   - 通过 `Response` 获取从服务器返回的数据。
4. **RealCall**:
   - `RealCall` 是 `Call` 接口的具体实现类。
   - 它表示一次实际的 HTTP 调用。
   - `RealCall` 负责将请求分派给 `Dispatcher` 并返回响应。
5. **Dispatcher**:
   - `Dispatcher` 负责调度和执行 `RealCall` 对象。
   - 它维护着一个请求队列，处理并发请求、限制同时运行的请求数量，管理空闲和正在运行的请求。
   - 默认情况下，`OkHttpClient` 使用一个共享的 `Dispatcher` 实例来处理所有请求。



##### Handler

Android 消息传递机制，用于线程间通信：比如将子线程拿到的数据通过hadler传递给主线程，从而完成主线程更新



- sendMessage                                        发送普通消息、延迟消息
- post                                                        
- dispatchMessage(Message msg)

###### Handler消息清除



##### Retrofit

@Get\@Post 等注解

使用步骤

1、构建ApiService定义请求的api模板（可以传入参数），传入参数格式，返回数据格式（自定义）

2、创建ApiClient客户端，即创建一个可以返回Retrofit实例的类（也可以直接在Activity里面定义）

```java
new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
```

3、使用，实例化apiService，选择想要访问的api，传入定义好的数据。接收返回数据，然后enqueue 完成回调函数onResponse、onFailure

```java
// 发起请求
        ApiService apiService = ApiClient.getApiService();
        Call<PostItems> call = apiService.login(username, password);
        call.enqueue(new Callback<PostItems>() {
            @Override
            public void onResponse(Call<PostItems> call, Response<PostItems> response) {
                Log.d("retrofit", "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    // 登录成功处理
                    Log.d("retrofit", "Login successful");
                } else {
                    // 登录失败处理
                    Log.e("retrofit", "Login failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PostItems> call, Throwable t) {
                // 网络请求失败处理
                Log.e("retrofit", "Login failed", t);
            }
        });
```



api就和后端框架类似，可以通过参数构建(使用@path注解)

```java
public interface ApiService {
    @GET("users/{id}")
    Call<User> getUser(@Path("id") String userId);
}

ApiService apiService = ApiClient.getApiService();
Call<User> call = apiService.getUser(userId);
```





##### Handler机制

###### 生产者-消费者模式

消息队列是 `Handler` 机制的核心组件之一，用于存储消息（`Message`）和可运行的任务（`Runnable`）。每个线程可以有一个 `MessageQueue` 实例。

- **消息队列（MessageQueue）**：一个先进先出（FIFO）队列，用于保存消息和任务。它可以被多个生产者（如子线程）写入消息，也可以被一个消费者（如主线程）读取和处理消息。

生产者是负责创建和发送消息的部分。在 Android 中，生产者通常是其他线程，它们通过 `Handler` 将消息发送到 `MessageQueue`。

- **生产者（Producer）**：创建消息或任务并将其放入消息队列。生产者可以是任何线程，它通过 `Handler` 向 `MessageQueue` 发送消息。

消费者是负责从消息队列中读取并处理消息的部分。在 Android 中，消费者通常是主线程，它使用 `Looper` 和 `Handler` 来处理消息队列中的消息。

- **消费者（Consumer）**：从消息队列中获取消息或任务并进行处理。消费者通常是主线程，它通过 `Looper` 不断检查消息队列中的新消息，并通过 `Handler` 的 `handleMessage` 方法处理消息。



###### 子线程使用hander

1. Looper.prepare();
2.  new Handler(Looper.myLooper())
3. Looper.loop();



###### ThreadLocal

线程内部的数据存储类



###### Handler、Looper、MessageQueue、线程的关系

一个线程对应的Looper数量对应关系

###### **一个线程对应的 Looper 数量关系**

- **一个线程只能有一个 `Looper`**：每个线程只能有一个 `Looper` 实例。一个 `Looper` 对象通过 `Looper.prepare()` 方法创建，并且只能在创建它的线程中使用。

**Looper 和 MessageQueue 数量关系**

- **一个 `Looper` 对应一个 `MessageQueue`**：每个 `Looper` 都有一个对应的 `MessageQueue`。当 `Looper.prepare()` 被调用时，会创建一个新的 `MessageQueue` 并与这个 `Looper` 关联。

**Handler 和 Looper 数量关系**

- **多个 `Handler` 可以共享一个 `Looper`**：一个 `Looper` 可以被多个 `Handler` 实例共享。每个 `Handler` 都需要一个 `Looper` 来将消息发送到该 `Looper` 的消息队列中。

###### **为什么主线程不用初始化Looper**

​	在 Android 中，主线程（也称为 UI 线程）是应用程序启动时自动创建的。在应用程序启动时，Android 框架已经为主线程初始化了 `Looper` 和 `MessageQueue`。这意味着在主线程中，可以直接使用 `Handler` 来处理消息和执行任务，而不需要手动初始化 `Looper`。

```java
public static void main(String[] args) {
    Looper.prepareMainLooper(); // 初始化主线程的 Looper
    ActivityThread thread = new ActivityThread();
    thread.attach(false);
    Looper.loop(); // 开始主线程的消息循环
}

```

###### **hadler是如何进行线程切换的**

**发送消息**：

- 在子线程中创建并发送一个消息（`Message`）到 `Handler`。这个消息会被添加到 `Handler` 所关联的 `Looper` 的 `MessageQueue` 中。

**消息入队**：

- `Handler` 将消息添加到 `Looper` 的 `MessageQueue` 中。`MessageQueue` 是一个先进先出（FIFO）的队列，用于保存待处理的消息。

**消息处理**：

- `Looper` 在其消息循环中不断地从 `MessageQueue` 中取出消息，并将消息分发给对应的 `Handler` 处理。

**处理消息**：

- `Handler` 的 `dispatchMessage` 方法调用 `handleMessage` 方法来处理消息。这个方法在 `Handler` 所关联的线程中执行（对于主线程来说，就是 UI 线程）。

###### SharedPreference

本地轻量级存储类 键值对方式存储数据的xml文件

/data/data/shared_pref 目录下

###### MMKV

MMKV 是由腾讯开源的高性能通用键值对存储库，基于 mmap 内存映射和 protobuf 序列化。MMKV 的主要优势在于其高效性和易用性。它可以替代传统的 `SharedPreferences`，提供更快的读写速度和更高的并发性能。

