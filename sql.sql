//查询用户钱包中的金额是否与用户获取并提现的红包总金额相等
/*SELECT `t_wallet`.`user_id`,`t_wallet`.`total_money`,a.`amount` FROM `t_wallet`,(
SELECT `user_id` AS 'user_id', SUM(`t_redenvelop_item`.`amount_money`) as 'amount' FROM `t_redenvelop_item` WHERE `t_redenvelop_item`.`extract_state` = 1 GROUP BY `t_redenvelop_item`.`user_id`
) AS a WHERE `t_wallet`.`user_id` = a.`user_id` AND `t_wallet`.`user_id` NOT IN (SELECT DISTINCT(`t_user_withdraw`.`user_id`) FROM `t_user_withdraw`) AND `t_wallet`.`total_money` <> a.`amount`*/