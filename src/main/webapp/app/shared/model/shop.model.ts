export interface IShop {
  id?: any;
  nameKo?: string;
  nameEn?: string;
  type?: string;
  workStartTime?: string;
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
  workStartTime: '',
  activated: true,
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
