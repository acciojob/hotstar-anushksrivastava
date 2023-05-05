package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription s=new Subscription();
        s.setId(subscriptionEntryDto.getUserId());
        s.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        s.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        String type=s.getSubscriptionType().toString();
        int amount=0;
        if(type==SubscriptionType.BASIC.toString())
        {
            amount=500+200*s.getNoOfScreensSubscribed();
        } else if(type==SubscriptionType.PRO.toString())
        {
            amount=800+250*s.getNoOfScreensSubscribed();
        } else if (type==SubscriptionType.ELITE.toString()) {
            amount=1000+350*s.getNoOfScreensSubscribed();

        }
s.setTotalAmountPaid(amount);
       // s.setStartSubscriptionDate(Date.from(Instant.now()));
        subscriptionRepository.save(s);

        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User s=userRepository.findById(userId).get();
        Subscription subscription=s.getSubscription();
        SubscriptionType type=s.getSubscription().getSubscriptionType();
        int diff=0;
        if(type.equals(SubscriptionType.ELITE))
        {
           throw new Exception("Already the best Subscription") ;
        }
        if(type==SubscriptionType.BASIC)
        {
           subscription.setSubscriptionType(SubscriptionType.PRO);
           diff=800+250*subscription.getNoOfScreensSubscribed()-subscription.getTotalAmountPaid();
            subscriptionRepository.save(subscription);
        }
        else if(type==SubscriptionType.PRO)
        {
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            diff=1000+350*subscription.getNoOfScreensSubscribed()-subscription.getTotalAmountPaid();
            subscriptionRepository.save(subscription);
        }
        return diff;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptions=subscriptionRepository.findAll();
        int revenue=0;
        for(int i=0;i<subscriptions.size();i++)
        {
            revenue+=subscriptions.get(i).getTotalAmountPaid();
        }

        return revenue;
    }

}
