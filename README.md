# JetPack之ViewModel

[TOC]

## 一、概念

### 1.1 `ViewModel`

`ViewModel` 类旨在以注重生命周期的方式存储和管理界面相关的数据。`ViewModel `类让数据可在发生屏幕旋转等配置更改后继续留存。

参考：[https://developer.android.google.cn/topic/libraries/architecture/viewmodel](https://developer.android.google.cn/topic/libraries/architecture/viewmodel)

#### 1.1.1 `ViewModel`生命周期

`ViewModel` 对象存在的时间范围是获取 `ViewModel `时传递给 `ViewModelProvider `的` Lifecycle`。`ViewModel `将一直留在内存中，直到限定其存在时间范围的` Lifecycle `永久消失：对于` Activity`，是在 `Activity `完成时；而对于 `Fragment`，是在` Fragment `分离时。

下图说明了` Activity `经历屏幕旋转而后结束的过程中所处的各种生命周期状态。该图还在关联的 `Activity `生命周期的旁边显示了` ViewModel` 的生命周期。此图表说明了` Activity `的各种状态。这些基本状态同样适用于 Fragment 的生命周期。 

![image](https://github.com/tianyalu/NeJetPackViewModel/raw/master/show/viewmodel-lifecycle.png)

#### 1.1.2 在`Fragment`之间共享数据

Activity 中的两个或更多 Fragment 需要相互通信是一种很常见的情况。想象一下主从 Fragment 的常见情况，假设您有一个 Fragment，在该 Fragment 中，用户从列表中选择一项，还有另一个 Fragment，用于显示选定项的内容。这种情况不太容易处理，因为这两个 Fragment 都需要定义某种接口描述，并且所有者 Activity 必须将两者绑定在一起。此外，这两个 Fragment 都必须处理另一个 Fragment 尚未创建或不可见的情况。

可以使用 ViewModel 对象解决这一常见的难点。这两个 Fragment 可以使用其 Activity 范围共享 ViewModel 来处理此类通信。

此方法具有以下优势：

> 1. `Activity` 不需要执行任何操作，也不需要对此通信有任何了解。
> 2. 除了 SharedViewModel 约定之外，Fragment 不需要相互了解。如果其中一个 Fragment 消失，另一个 Fragment 将继续照常工作。
> 3. 每个 Fragment 都有自己的生命周期，而不受另一个 Fragment 的生命周期的影响。如果一个 Fragment 替换另一个 Fragment，界面将继续工作而没有任何问题。

### 1.2 `ViewModel`优势

* 数据管理
* `Activity`重建数据不丢失

## 二、实操

### 2.1 `Activity`重建数据不丢失

`MainActivity`：

```java
public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn3;
    private Button btnMain2;
    private TextView tvResult;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListeners();
    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn3 = findViewById(R.id.btn3);
        btnMain2 = findViewById(R.id.btn_main2);
        tvResult = findViewById(R.id.tv_result);

        //不能这样初始化ViewModel，这样的话，就没有任何ViewModel的功能，就和普通的类没有什么区别了
        //mainViewModel = new MainViewModel();
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(MainViewModel.class);

        //横竖屏切换时会重新执行生命周期函数，这里可以保证切换时数据不丢失
        tvResult.setText(String.valueOf(mainViewModel.number));

    }

    private void initListeners() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.number++;
                tvResult.setText(String.valueOf(mainViewModel.number));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.number += 3;
                tvResult.setText(String.valueOf(mainViewModel.number));
            }
        });

        btnMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
    }
}
```

`MainActivityViewModel`:

```java
/**
 * 对数据进行管理
 * 放到这里的数据在进行横竖屏切换时数据不会丢失
 * @Author: tian
 * @UpdateDate: 2020/11/6 9:12 PM
 */
public class MainViewModel extends ViewModel {

    public int number = 0;

    //正规的写法
    private MutableLiveData<Integer> number2;

    //不建议的写法
    //规则：不建议把LiveData暴露出去给V层用，建议把子类MutableLiveData给暴露出去
    private LiveData<Integer> number3;

    //自定义的LiveData可以使用
    private CustomLiveData<Integer> number4;
}
```

### 2.2 `ViewModel`结合`LiveData`使用

`Main2Activity`：

```java
public class Main2Activity extends AppCompatActivity {
    private Button btn1;
    private Button btn3;
    private TextView tvResult;
    private Main2ViewModel main2ViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
        initListeners();
    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn3 = findViewById(R.id.btn3);
        tvResult = findViewById(R.id.tv_result);

        //不能这样初始化ViewModel，这样的话，就没有任何ViewModel的功能，就和普通的类没有什么区别了
        //main2ViewModel = new main2ViewModel();
        main2ViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(Main2ViewModel.class);

        //横竖屏切换时会重新执行生命周期函数，这里可以保证切换时数据不丢失
        //通过LiveData的感应去更新UI
        main2ViewModel.getLiveDataNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvResult.setText(String.valueOf(integer));
            }
        });
    }

    private void initListeners() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main2ViewModel.addNumber(1);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main2ViewModel.addNumber(3);
            }
        });
    }
}
```

`Main2ActivityViewModel`:

```java
public class Main2ViewModel extends ViewModel {
    private MutableLiveData<Integer> liveDataNumber;

    //对外暴露的时候就判断初始化
    public MutableLiveData<Integer> getLiveDataNumber() {
        if(liveDataNumber == null) {
            liveDataNumber = new MutableLiveData<>();
            liveDataNumber.setValue(0);
        }
        return liveDataNumber;
    }

    //对外暴露的
    public void addNumber(int number) {
        liveDataNumber.setValue(liveDataNumber.getValue() + number);
    }
}
```

