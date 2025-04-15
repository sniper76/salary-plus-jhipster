export interface ISalaryDetail {
  date?: string;
  salaryPrice?: number;
  penaltyPrice?: number;
}

export const defaultValue: Readonly<ISalaryDetail> = {
  date: '',
  salaryPrice: 0,
  penaltyPrice: 0,
};
