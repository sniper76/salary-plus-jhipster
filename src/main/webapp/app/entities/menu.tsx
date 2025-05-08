import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import {
  faAtlas,
  faBath,
  faBed,
  faCapsules,
  faHelmetUn,
  faJoint,
  faMoneyBill,
  faMoneyCheckDollar,
  faRodSnake,
  faSquare,
  faStamp,
  faWineGlass,
} from '@fortawesome/free-solid-svg-icons';

import { Translate } from 'react-jhipster';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
      <MenuItem icon={faCapsules} to="/shop/sales-item">
        <Translate contentKey="global.menu.management.salesItem">상품관리</Translate>
      </MenuItem>
      <MenuItem icon={faHelmetUn} to="/shop/penalty">
        <Translate contentKey="global.menu.management.penalty">벌금관리</Translate>
      </MenuItem>
      <MenuItem icon={faAtlas} to="/shop/base-salary">
        <Translate contentKey="global.menu.management.baseSalary">일당관리</Translate>
      </MenuItem>
      {/* <MenuItem icon={faMoneyBill} to="/shop/bonus-salary">
        <Translate contentKey="global.menu.management.bonusSalary">보너스관리</Translate>
      </MenuItem> */}
      <MenuItem icon={faMoneyCheckDollar} to="/shop/sales-item-discount">
        <Translate contentKey="global.menu.management.salesItemDiscount">할인관리</Translate>
      </MenuItem>
      <MenuItem icon={faStamp} to="/shop/user-base-salary-mapping">
        <Translate contentKey="global.menu.management.userBaseSalaryMapping">사용자기본일당연결</Translate>
      </MenuItem>
      <MenuItem icon="user" to="/shop/model-management">
        <Translate contentKey="global.menu.management.model">모델관리</Translate>
      </MenuItem>
    </>
  );
};

export default EntitiesMenu;
