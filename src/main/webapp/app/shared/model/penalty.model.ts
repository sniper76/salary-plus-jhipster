export interface IShopPenalty {
  id?: any;
  shopId?: any;
  nameKo?: string;
  nameEn?: string;
  activated?: boolean;
  price?: number;
  type?: string;
  typeValue?: string;
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
  activated: true,
  price: 0,
  type: '',
  typeValue: '',
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
