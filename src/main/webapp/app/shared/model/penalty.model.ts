export interface IShopPenalty {
  id?: any;
  shopId?: any;
  nameKo?: string;
  nameEn?: string;
  isCommissionTarget?: boolean;
  isSnack?: boolean;
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

export const defaultValue: Readonly<IShopPenalty> = {
  id: '',
  shopId: '',
  nameKo: '',
  nameEn: '',
  isCommissionTarget: false,
  isSnack: false,
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
