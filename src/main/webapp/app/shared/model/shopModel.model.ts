export interface IShopModel {
  shopId?: any;
  csvData?: string;
}

export const defaultValue: Readonly<IShopModel> = {
  shopId: '',
  csvData: '',
};
