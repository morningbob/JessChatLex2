package com.bitpunchlab.android.jesschatlex2

import android.app.Application
import android.content.Context
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify

class MyAmplifyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            //Amplify.configure(applicationContext)
            //configureAmplify(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }

    // won't configure Amplify, I use AWS Mobile Client
    private fun configureAmplify(context: Context) {
        Amplify.addPlugin(AWSCognitoAuthPlugin())
        Amplify.configure(context)
    }
}