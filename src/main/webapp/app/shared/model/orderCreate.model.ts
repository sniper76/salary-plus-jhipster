export interface IOrderCreate {
  shopId?: any;
  orderId?: any;
  date?: string;
  modelIds?: any[];
  salesItemIds?: any[];
  salesItemDiscountIds?: any[];
  prices?: any[];
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
}

export const defaultValue: Readonly<IOrderCreate> = {
  shopId: '',
  orderId: '',
  date: '',
  modelIds: [],
  salesItemIds: [],
  salesItemDiscountIds: [],
  prices: [],
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
