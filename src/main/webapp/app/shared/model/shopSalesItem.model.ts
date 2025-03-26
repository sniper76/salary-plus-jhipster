export interface IShopSalesItem {
  id?: any;
  shopId?: any;
  nameKo?: string;
  nameEn?: string;
  commissionTargetYn?: boolean;
  snackYn?: boolean;
  activated?: boolean;
  price?: number;
  shopCommissionPrice?: number;
  mamaCommissionPrice?: number;
  modelCommissionPrice?: number;
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
}

export const defaultValue: Readonly<IShopSalesItem> = {
  id: '',
  shopId: '',
  nameKo: '',
  nameEn: '',
  commissionTargetYn: false,
  snackYn: false,
  activated: true,
  price: 0,
  shopCommissionPrice: 0,
  mamaCommissionPrice: 0,
  modelCommissionPrice: 0,
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
