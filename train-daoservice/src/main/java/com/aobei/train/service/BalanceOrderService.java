package com.aobei.train.service;

import com.aobei.train.model.BalanceOrderExample;
import com.aobei.train.model.BalanceOrder;
import com.aobei.train.model.Users;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface BalanceOrderService extends MbgReadService<Long, BalanceOrder, BalanceOrder, BalanceOrderExample>,MbgWriteService<Long, BalanceOrder, BalanceOrder, BalanceOrderExample>,MbgUpsertService<Long, BalanceOrder, BalanceOrder, BalanceOrderExample>{

    String selectMaxBalanceCycle();

    int xAddFallinto(String str);

    int xHangUp(Long balance_order_id,Users users);
}