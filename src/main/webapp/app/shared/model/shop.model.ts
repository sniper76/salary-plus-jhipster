export interface IShop {
  id?: any;
  nameKo?: string;
  nameEn?: string;
  type?: string;
  activated?: boolean;
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
}

export const defaultValue: Readonly<IShop> = {
  id: '',
  nameKo: '',
  nameEn: '',
  type: '',
  activated: true,
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
