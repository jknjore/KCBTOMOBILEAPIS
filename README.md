ENDPOINTS
main api is /mobile
mpesa callback api is /mpesa/callback

APPLICATION.PROPERTIES DESCRIPTIONS
context path is /v1  
ASSUMTIONS   
maxSmsSendRetries: 1 #max no of times to retry sending an sms
minSmsRetryIntervalMins: 5 #min time in mins before trying resending an sms to customer
maxSmsRetryLifespanMins: 15 #max no of times to retry sending an sms
smsRetryIterationMins: 5 #global time in mines for fetching sms records that deserve retry

SMS SENDING PROCESS
1. Mpesa sms are sent after callback
2. Airtel sms are sent on the fly

ASSUMPTIONS
Airtel gives a synscronous response to bank to mobile request
Mpesa provides asynchronous request
calltoAirtelAPIs service will make external calls for airtel
calltoMpesaAPIs service willl make external calls for mpesa


DATABASE USED
currently MYSQL, run out of time to change config file to use H2



