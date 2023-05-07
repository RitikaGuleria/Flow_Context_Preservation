package com.example.flowcontextpreservationandtrycatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.Main) {
            producer()
                .map{
                    delay(1000)
                    it*2
                    Log.d("Log","Map Thread- ${Thread.currentThread().name}")
                }
                .flowOn(Dispatchers.IO)
                .filter{
                    delay(500)
                    Log.d("Log","filter Thread- ${Thread.currentThread().name}")
                    it<8
                }
                .flowOn(Dispatchers.IO)
                .collect(){
                Log.d("Log","Collect Thread- ${Thread.currentThread().name}")
            }
        }
    }

    private fun producer() : Flow<Int> {
        return flow<Int>{
            val list= listOf(1,2,3,4,5)
            list.forEach {
                delay(1000)
                Log.d("Log","Emitter thread- ${Thread.currentThread().name}")
                emit(it)
            }
        }
    }
}