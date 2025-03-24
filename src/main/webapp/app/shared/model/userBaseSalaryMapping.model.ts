export interface IUserBaseSalaryMapping {
  shopId?: any;
  userId?: any;
  shopBaseSalaryId?: any;
}

export const defaultValue: Readonly<IUserBaseSalaryMapping> = {
  shopId: '',
  userId: '',
  shopBaseSalaryId: '',
};
