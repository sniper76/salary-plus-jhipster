export interface IShopBaseSalary {
  id?: any;
  shopId?: any;
  nameKo?: string;
  nameEn?: string;
  activated?: boolean;
  price?: number;
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
}

export const defaultValue: Readonly<IShopBaseSalary> = {
  id: '',
  shopId: '',
  nameKo: '',
  nameEn: '',
  activated: true,
  price: 0,
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
