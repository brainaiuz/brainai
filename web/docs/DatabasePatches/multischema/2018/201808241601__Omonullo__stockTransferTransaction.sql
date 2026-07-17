update "anv".transaction
set dtype = 'EdsStockTransferTransaction', stocktransferid = sa.stocktransferid
from "anv".stock_adjustment as sa
where sa.id = adjustment_id
      and dtype = 'EdsStockAdjustmentTransaction'
      and adjustment_id is not null and (select stocktransferid
                                         from "anv".stock_adjustment
                                         where id = adjustment_id) is not null;

update "0".transaction
set dtype = 'EdsStockTransferTransaction', stocktransferid = sa.stocktransferid
from "0".stock_adjustment as sa
where sa.id = adjustment_id
      and dtype = 'EdsStockAdjustmentTransaction'
      and adjustment_id is not null and (select stocktransferid
                                         from "0".stock_adjustment
                                         where id = adjustment_id) is not null;