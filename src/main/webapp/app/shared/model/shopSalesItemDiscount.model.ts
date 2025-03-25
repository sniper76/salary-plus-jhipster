export interface IShopSalesItemDiscount {
  id?: any;
  shopId?: any;
  salesItemId?: any;
  nameKo?: string;
  nameEn?: string;
  type?: string;
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

export const defaultValue: Readonly<IShopSalesItemDiscount> = {
  id: '',
  shopId: '',
  salesItemId: '',
  nameKo: '',
  nameEn: '',
  type: '',
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
