package com.passGuardian.passGuardian.repository;

import com.passGuardian.passGuardian.models.PassWordsModel;
import com.passGuardian.passGuardian.models.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassWordsRepository extends JpaRepository<PassWordsModel, Long>{
    List<PassWordsModel> findByUserId(UsersModel userId);

    List<PassWordsModel> findByUserIdAndFavorite(UsersModel user, String favorite);
}
