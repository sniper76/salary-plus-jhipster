export const AUTHORITIES = {
  ADMIN: 'ROLE_ADMIN',
  USER: 'ROLE_USER',
  MANAGER: 'ROLE_MANAGER',
  INVESTOR: 'ROLE_INVESTOR',
  CEO: 'ROLE_CEO',
  DJ: 'ROLE_DJ',
  MAMA: 'ROLE_MAMA',
  MODEL: 'ROLE_MODEL',
  WAITRESS: 'ROLE_WAITRESS',
  SUPERVISOR: 'ROLE_SUPERVISOR',
};

export const messages = {
  DATA_ERROR_ALERT: 'Internal Error',
};

export const APP_DATE_FORMAT = 'DD/MM/YY HH:mm';
export const APP_TIMESTAMP_FORMAT = 'DD/MM/YY HH:mm:ss';
export const APP_LOCAL_DATE_FORMAT = 'DD/MM/YYYY';
export const APP_LOCAL_TIMESTAMP_FORMAT = 'DD/MM/YYYY HH:mm';
export const APP_LOCAL_DATETIME_FORMAT = 'YYYY-MM-DDTHH:mm';
export const APP_WHOLE_NUMBER_FORMAT = '0,0';
export const APP_TWO_DIGITS_AFTER_POINT_NUMBER_FORMAT = '0,0.[00]';

export const bonusTypes: any = [
  { key: 'PLUS_SALARY', name: '일당추가' },
  { key: 'SALARY_BONUS', name: '일단보너스' },
  { key: 'COMMISSION', name: '커미션보너스' },
];

export const AUTHORITY_LIST = [
  { key: 'ROLE_ADMIN', name: 'ADMIN' },
  { key: 'ROLE_USER', name: 'USER' },
  { key: 'ROLE_MANAGER', name: 'MANAGER' },
  { key: 'ROLE_INVESTOR', name: 'INVESTOR' },
  { key: 'ROLE_CEO', name: 'CEO' },
  { key: 'ROLE_DJ', name: 'DJ' },
  { key: 'ROLE_MAMA', name: 'MAMA' },
  { key: 'ROLE_MODEL', name: 'MODEL' },
  { key: 'ROLE_WAITRESS', name: 'WAITRESS' },
  { key: 'ROLE_SUPERVISOR', name: 'SUPERVISOR' },
];

export const getBonusTypeName = (e: string) => bonusTypes.filter((id: any) => id.key === e).map((id: any) => id.name);

export const getAuthorityName = (e: string) => AUTHORITY_LIST.filter((id: any) => id.key === e).map((id: any) => id.name);
