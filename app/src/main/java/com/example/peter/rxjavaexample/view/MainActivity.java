package com.example.peter.rxjavaexample.view;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.peter.rxjavaexample.R;
import com.example.peter.rxjavaexample.model.Student;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Disposable disposable;
    private Observable<List<Student>> mStudentObservable;
    private Observer<Student> mStudentObserver;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create student ObserveableData
        mStudentObservable = Observable.just(getStudentsList());

        //Create an Observer to watch on student
        mStudentObserver = getStudentObserver();

        //subscribe to the Obseveable
        mStudentObservable
                .flatMap(Observable::fromIterable)
                .filter(student -> (student.isAlumni() == true))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(mStudentObserver);


    }

    @NonNull
    private Observer<Student> getStudentObserver() {
        return new Observer<Student>() {
            @Override
            public void onSubscribe(Disposable d) {
              disposable = d;
            }

            @Override
            public void onNext(Student student) {
                Log.e("Alumni__", ""+student.getUsername());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e("Alumni__", "emission Complete");
            }
        };
    }

    private List<Student> getStudentsList() {
        List<Student> studentsList = new ArrayList<>();
        Student peter = new Student();
        peter.setUsername("Mavs");
        peter.setAge("20");
        peter.setAlumni(true);

        Student mike = new Student();
        mike.setUsername("Mike Owen");
        mike.setAge("20");
        mike.setAlumni(false);

        Student linda = new Student();
        linda.setUsername("Linda");
        linda.setAge("20");
        linda.setAlumni(true);

        Student johnte = new Student();
        johnte.setUsername("Johnte");
        johnte.setAge("20");
        johnte.setAlumni(false);

        studentsList.add(peter);
        studentsList.add(mike);
        studentsList.add(linda);
        studentsList.add(johnte);
        return studentsList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
