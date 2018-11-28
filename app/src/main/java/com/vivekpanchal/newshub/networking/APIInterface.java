package com.vivekpanchal.newshub.networking;

import com.vivekpanchal.newshub.models.NewsHeadlineResponse;
import com.vivekpanchal.newshub.utility.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET(Constants.TOP_HEADLINES_API_REQUEST)
    Call<NewsHeadlineResponse> getTopNewsHeadlines(@Query(Constants.QUERY_PARAM_COUNTRY_API) String country,
                                                   @Query(Constants.QUERY_PARAM_API_KEY) String apiKey,
                                                   @Query(Constants.QUERY_PARAM_LANGUAGE) String language,
                                                   @Query(Constants.QUERY_PARAM_FROM_DATE) String fromDate,
                                                   @Query(Constants.QUERY_PARAM_TO_DATE) String toDate);


    @GET(Constants.EVERYTHING_API_REQUEST)
    Call<NewsHeadlineResponse> getNewsHeadlineOfQuery(@Query(Constants.QUERY_PARAM_QUERY) String query,
                                                      @Query(Constants.QUERY_PARAM_API_KEY) String apiKey,
                                                      @Query(Constants.QUERY_PARAM_LANGUAGE) String language,
                                                      @Query(Constants.QUERY_PARAM_FROM_DATE) String fromDate,
                                                      @Query(Constants.QUERY_PARAM_TO_DATE) String toDate);
}
