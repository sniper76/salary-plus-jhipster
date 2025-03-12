export interface IShopSalesItem {
  id?: any;
  nameKo?: string;
  nameEn?: string;
  isCommissionTarget?: boolean;
  isSnack?: boolean;
  activated?: boolean;
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
}

export const defaultValue: Readonly<IShopSalesItem> = {
  id: '',
  nameKo: '',
  nameEn: '',
  isCommissionTarget: false,
  isSnack: false,
  activated: true,
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
