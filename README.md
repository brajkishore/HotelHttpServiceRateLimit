# HotelHttpServiceRateLimit
Agoda Backend Engineer Hiring assigment<br/></i>Project is based on Java Spring boot 4.1</i><br/>
<br/>
<b>Problem Statement :</b><br/>

You are provided with hotels database in CSV (Comma Separated Values) format.<br/>
We need you to implement HTTP service, according to the API requirements described below. You may use any language or platform that you like: C#/Java/Scala/etc.<br/>
<ol><li>
<b>RateLimit:</b> API calls need to be rate limited (request per 10 seconds) based on API Key provided in each http call.<br/>
<ul><li>On exceeding the limit, api key must be suspended for next 5 minutes.</li><li>
Api key can have different rate limit set, in this case from configuration, and if not present there must be a global rate limit applied.
</li>
</ul>
</li>
<li>
Search hotels by CityId<br/>
</li>
<li>
Provide optional sorting of the result by Price (both ASC and DESC order).<br/></li>
</ol>
<b>Note:</b> <i>Please don’t use any external library or key-value store for RateLimit. You need to create a InMemory Implementation for RateLimit.</i>

----------------------------------- Run ---------------------------
> Download the project src and use maven-3.3.3 and java 8 to compile the code and run
<br/>
or
<br/>
>Download the HotelHttpService-0.0.1.jar , hoteldb.csv , application.properties and put all under the same directory.<br/>
>run java -jar HotelHttpService-0.0.1.jar
<br/>

<b>Api Exposed:</b><br/>
1. To get Hotels by cityId ( Generated based on city name ) with options of sorting order<br/>
</i>GET /hotels/{apiKey}/{cityId}?sort=DESC or ASC </i><br/><br/>
2. To generate Unique API key
</i>POST /apiKeys</i><br/>
{
	"apiKey":"braj",
	"maxLimit":2,
	"perRequestIntervalInMillis":5000,
	"suspensionIntervalInMillis":30000
}
<br/><br/>
3. To delete Unique API key
</i>DELETE /apiKeys/{apiKey}</i> <br/>

<b>Note:</b> Defualt tomcat listener port is 8080





