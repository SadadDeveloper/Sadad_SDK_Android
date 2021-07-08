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
    }
            
How to Use ?

	1) Prepare sadad Order :

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

	2) Initiate service :

	SadadService.getProductionService();

	3) Create Transaction :

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
							

After downloading and setting up SDK, below are some code snippets which might be helpful to build the checkout flow.

	4) Generating Access Token:
	
	
		public void generateToken() {
		    String url; 
		    url = ServerConfig.SERVER_TOKEN_URL;
		    RestClient.getInstance().post(HomeActivity.this, url, RequestMethod.POST, true, 
			RequestCode.GENERATE_TOKEN, false,
			new DataObserver() {
			    @Override 
				public void onSuccess(RequestCode mRequestCode, Object mObject) {
				    try { 
					JSONObject jsonObject = new JSONObject(mObject.toString());
					String token = jsonObject.optString(Constant.ACCESS_TOKEN, ""); startNewActivity(token);
				    }
				    catch (JSONException e) { 
					e.printStackTrace(); 
					showSnackBar(rootView, "Invalid Token", getString(R.string.str_ok), true, null); 
				    }
				}   
			    @Override 
				public void onFailure(RequestCode mRequestCode, String mError, int errorCode) {
				    showSnackBar(rootView, mError, getString(R.string.str_ok), true, null);
				}
			)};
		}
	
	5) Getting transaction response using SDK :
		
		@Override
		    public void onTransactionResponse(String inResponse) {

			int transactionStatusId = Constant.TRANSACTION_STATUS_ID_FAILED;
			double amount = totalAmount;
			String transactionNumber = "";

		    JSONObject jsonObject;
			try { 
			    jsonObject = new JSONObject(inResponse);
			    if (jsonObject.has("data") && !jsonObject.isNull("data")) {
				JSONObject dataJson = jsonObject.optJSONObject("data");
			    }
			    if (!dataJson.isNull("transactionstatus")) {
				transactionStatusId = dataJson.optInt("transactionstatus"); 
			    }
			    else { 
				transactionStatusId = dataJson.optInt("transactionstatusId"); 
			    }

			    amount = dataJson.optDouble("amount");

			    if (!dataJson.isNull("transactionnumber")) { 
				transactionNumber = dataJson.optString("transactionnumber"); 
			    }
			    else { 
				transactionNumber = dataJson.optString("invoicenumber"); 
			    }
			}
		    }
			catch (JSONException e) { 
			    e.printStackTrace(); 
			}
			Intent intent = new Intent(HomeActivity.this, TransactionStatusActivity.class); 
			intent.putExtra(Constant.TRANSACTION_STATUS, transactionStatusId); 
			intent.putExtra(Constant.AMOUNT, amount); 
			intent.putExtra(Constant.TRANSACTION_ID, transactionNumber); 
			startActivitywithAnimation(intent, false);

		}
		
In the above **public void onTransactionResponse(String inResponse)** you will notice the **transactionStatusId** variable. 
Your transaction is successful or failed is depends on it. The codes are stated belows :

1) transactionStatusId = 3 means the transaction got success
2) transactionStatusId = 2 means the transaction got failed


