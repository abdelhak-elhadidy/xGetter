# xGetter => LowCostVideo [3.0]
I change something to comply with Google play policy.
- Remove external javascript file
- Change the class name to LowCostVideo
- Rewrite codes

# xGetter Library for Android!  
[![](https://jitpack.io/v/KhunHtetzNaing/XGetter.svg)](https://jitpack.io/#KhunHtetzNaing/XGetter)  

For those who want to create a movie and video app
This is a useful library.
No need to look at ads
You can download the link to download it directly
Will you download the link directly?
Show live with your own player?
You can use it as you see fit.
Currently ten sites are:
  
 - **1. Google Drive**  
 - **2. DailyMotion**  
 - **3. Google Photos**  
 - **4. Mp4Upload**  
 - **5. Facebook**  
 - **6. Mediafire**  
 - **7. Ok.Ru**  
 - **8. VK**  
 - **9. Twitter**  
 - **10. ~~Youtube~~**  
 - **11. SolidFiles**  
 - **12. Vidoza**  
 - **13. UptoStream**  
 - **14. SendVid**  
 - **15. FanSubs**  
 - **16. Uptobox**  
 - **17. FEmbed**  
 - **18. FileRio**  
 - **19. MegaUp**  
 - **20. GoUnlimited**  
 - **21. CocoScope**  
 - **22. VidBM**
 
## New supported sites [1.0]

- **23. Vlare**
- **24. pStream**
- **25. Vivo.sx**
- **26. VideoBin**
- **27. BitTube**
- **28. 4Shared**
- **29. StreamTape**
- **30. Vudeo**
  
  
We are the only ones.
All of these sites provide direct links to ads without the need to view ads.
We will add other free video uploading sites :)
So you do not need to buy a host for the Video Sharing App;)
  
How to use
===========
  
From the first ** build.gradle (project) **
  

    allprojects {
      repositories {  
      google()  
            jcenter()  
            maven { url "https://jitpack.io" }  //Add this
     }}

Then **build.gradle(app)** Out of 
  

    dependencies {  
    	implementation 'com.github.KhunHtetzNaing:xGetter:3.0'
    }

For Android Studio users, please Sync Now **.
If you are an AIDE user, please save and download.
**Note. You need to have internet enabled **
  
   If not working [download this jar] (https://github.com/KhunHtetzNaing/xGetter/raw/master/app/release/xgetter.jar) file and put to ** YourProject / app / libs / **
  
In ** AndroidManifest.xml **
  

     <application .....
	     android:usesCleartextTraffic="true">

  
You need to add
  
Then from the Activity you want to call

    LowCostVideo xGetter = new LowCostVideo(this);  
    xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {  
        @Override  
      public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) {  
            if (multiple_quality){ //This video you can choose qualities  
      for (XModel model : vidURL){  
                    String url = model.getUrl();   
     String cookie = model.getCookie(); //If google drive video you need to set cookie for play or download  
      }   
            }else {//If single  
      String url = vidURL.get(0).getUrl();  
      }  
        }  
      
        @Override  
      public void onError() {  
            //Error  
      }  
    });

## IMPORTANT
Because Okhttp3 is used
** proguard-rules.pro ** is required.
  

      # JSR 305 annotations are for embedding nullability information.  
    -dontwarn javax.annotation.**  
      
    # A resource is loaded with a relative path so the package of this class must be preserved.  
    -keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase  
      
    # Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.  
    -dontwarn org.codehaus.mojo.animal_sniffer.*  
      
    # OkHttp platform used only on JVM and when Conscrypt dependency is available.  
    -dontwarn okhttp3.internal.platform.ConscryptPlatform

