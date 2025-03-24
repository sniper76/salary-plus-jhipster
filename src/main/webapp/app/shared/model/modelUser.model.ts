export interface IModelUser {
  id?: any;
  login?: string;
  firstName?: string;
  lastName?: string;
  modelNo?: string;
  price?: number;
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
}

export const defaultValue: Readonly<IModelUser> = {
  id: '',
  login: '',
  firstName: '',
  lastName: '',
  modelNo: '',
  price: 0,
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
