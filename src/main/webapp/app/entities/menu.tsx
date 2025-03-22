import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { Translate } from 'react-jhipster';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
      <MenuItem icon="users" to="/shop/sales-item">
        <Translate contentKey="global.menu.management.salesItem">상품관리</Translate>
      </MenuItem>
      <MenuItem icon="users" to="/shop/penalty">
        <Translate contentKey="global.menu.management.penalty">벌금관리</Translate>
      </MenuItem>
      <MenuItem icon="users" to="/shop/base-salary">
        <Translate contentKey="global.menu.management.baseSalary">일당관리</Translate>
      </MenuItem>
    </>
  );
};

export default EntitiesMenu;
