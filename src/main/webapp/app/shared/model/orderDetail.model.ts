export interface IOrderDetail {
  id?: any;
  orderDetailId?: any;
  modelId?: any;
  nameKo?: string;
  nameEn?: string;
  activated?: boolean;
  commissionTargetYn?: boolean;
  snackYn?: boolean;
  price?: number;
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
}

export const defaultValue: Readonly<IOrderDetail> = {
  id: '',
  orderDetailId: '',
  modelId: '',
  nameKo: '',
  nameEn: '',
  activated: true,
  commissionTargetYn: true,
  snackYn: true,
  price: 0,
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
};
