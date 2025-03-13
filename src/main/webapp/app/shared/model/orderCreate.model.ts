export interface IOrderCreate {
  shopId?: any;
  date?: string;
  modelIds?: any[];
  salesItemIds?: any[];
  prices?: any[];
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
}

export const defaultValue: Readonly<IOrderCreate> = {
  shopId: '',
  date: '',
  modelIds: [],
  salesItemIds: [],
  prices: [],
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
