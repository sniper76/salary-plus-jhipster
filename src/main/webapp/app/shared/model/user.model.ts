export interface IUser {
  id?: any;
  login?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  activated?: boolean;
  commissionTargetYn?: boolean;
  discountAcceptYn?: boolean;
  langKey?: string;
  modelNo?: string;
  authorities?: any[];
  shops?: any[];
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
  password?: string;
}

export const defaultValue: Readonly<IUser> = {
  id: '',
  login: '',
  firstName: '',
  lastName: '',
  email: '',
  activated: true,
  commissionTargetYn: true,
  discountAcceptYn: true,
  langKey: '',
  modelNo: '',
  authorities: [],
  shops: [],
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
  password: '',
};
