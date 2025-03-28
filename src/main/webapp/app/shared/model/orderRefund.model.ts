export interface IOrderRefund {
  shopId?: any;
  orderId?: any;
  date?: string;
  shopPrice?: number;
  modelPrice?: number;
  mamaPrice?: number;
}

export const defaultValue: Readonly<IOrderRefund> = {
  shopId: '',
  orderId: '',
  date: '',
  shopPrice: 0,
  modelPrice: 0,
  mamaPrice: 0,
};
