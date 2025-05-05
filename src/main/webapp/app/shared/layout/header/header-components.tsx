import React from 'react';
import { Translate } from 'react-jhipster';

import { NavbarBrand, NavItem, NavLink } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/salary-plus-logo.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="navbar-version">{VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Work = () => (
  <NavItem>
    <NavLink tag={Link} to="/work" className="d-flex align-items-center">
      <FontAwesomeIcon icon="book" />
      <span>
        <Translate contentKey="global.menu.work">Work</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Order = () => (
  <NavItem>
    <NavLink tag={Link} to="/order" className="d-flex align-items-center">
      <FontAwesomeIcon icon="bell" />
      <span>
        <Translate contentKey="global.menu.order">Order</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Sales = () => (
  <NavItem>
    <NavLink tag={Link} to="/sales" className="d-flex align-items-center">
      <FontAwesomeIcon icon="bell" />
      <span>
        <Translate contentKey="global.menu.sales">매출</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Salary = () => (
  <NavItem>
    <NavLink tag={Link} to="/salary" className="d-flex align-items-center">
      <FontAwesomeIcon icon="bell" />
      <span>
        <Translate contentKey="global.menu.salary">주급</Translate>
      </span>
    </NavLink>
  </NavItem>
);
