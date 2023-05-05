package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        WebSeries webSeries=new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setProductionHouse(productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        webSeries.setRating(webSeriesEntryDto.getRating());
        if(webSeriesRepository.findBySeriesName(webSeries.getSeriesName())!=null)
        {
            throw new Exception("Series is already present");
        }
        else
        {
            webSeriesRepository.save(webSeries);
            ProductionHouse p=webSeries.getProductionHouse();
            int n=p.getWebSeriesList().size();



                double ratings=p.getRatings();
                ratings=(ratings+webSeries.getRating())/n+1;
                p.setRatings(ratings);
               // productionHouseRepository.save(p);
        }


        return null;
    }

}
