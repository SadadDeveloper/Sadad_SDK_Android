# Sadad_SDK_Android
[![](https://jitpack.io/v/SadadDeveloper/Sadad_SDK_Android.svg)](https://jitpack.io/#SadadDeveloper/Sadad_SDK_Android)

Prerequisite : 
  Min Sdk version : 21
  
Steps to Integration :

  Step 1. Add the JitPack repository to your project level build.gradle file
  
    allprojects { 
      repositories { 
      maven {
          url 'https://jitpack.io' 
          }
       } 
    }

  Step 2. Add the dependency
  
    dependencies {
        implementation 'com.github.SadadDeveloper:Sadad_SDK_Android:latest-version'
	implementation 'com.google.android.material:material:1.1.0-alpha09'
    }
    
Step 3. Change Project App theme
  
   <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.MaterialComponents.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
    
How to Use ?

	1) Prepare sadad Order.

	Bundle bundle = new Bundle();
	bundle.putString(SadadOrder.ACCESS_TOKEN, token);
	bundle.putString(SadadOrder.CUST_ID, "123456789");
	bundle.putString(SadadOrder.ORDER_ID, "528963147");
	bundle.putString(SadadOrder.TXN_AMOUNT, totalAmount);
	bundle.putString(SadadOrder.MOBILE_NO, "9824672292");

	JSONArray productDetails = getProductDetails();

	if (productDetails.length() > 0) {
		bundle.putString(SadadOrder.ORDER_DETAIL, String.valueOf(productDetails));
	}

	//building product details e.g. product name, product quantity, product amount
	JSONArray getProductDetails() {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("itemname", "Gucci perfume");
			jsonObject.put("quantity", 1);
			jsonObject.put("amount", 1.0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jsonArray.put(jsonObject);
		return jsonArray;
	}

	SadadOrder sadadOrder = new SadadOrder();
	sadadOrder.setRequestParamMap(bundle);

	2) Initiate service.

	SadadService.getProductionService();

	3) Create Transaction.

	SadadService.createTransaction(HomeActivity.this, sadadOrder, new TransactionCallBack() {
									@Override
									public void onTransactionResponse(String inResponse){

									}

									@Override
									public void onBackPressedCancelTransaction() {

									}

									@Override
									public void onTransactionCancel(String errorJson) {

									}

									@Override
									public void onTransactionFailed(String errorJson) {

									}
							});
