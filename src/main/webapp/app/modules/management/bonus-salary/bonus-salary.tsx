import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getPaginationState, JhiItemCount, JhiPagination, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';

import { APP_LOCAL_TIMESTAMP_FORMAT, getBonusTypeName } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUserShops } from 'app/modules/order/order.reducer';
import { getPageShopBonusSalaries } from './bonus-salary.reducer';

import '../management.scss';

export const BonusSalary = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const shops = useAppSelector(state => state.orders.shops);
  const bonusSalariesForPage = useAppSelector(state => state.bonusSalaries.bonusSalariesForPage);
  const totalItems = useAppSelector(state => state.bonusSalaries.totalItems);
  const loading = useAppSelector(state => state.bonusSalaries.loading);

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedValue, setSelectedValue] = useState(-1);

  const getUsersFromProps = () => {
    console.warn('getUsersFromProps', selectedValue, this);
    dispatch(
      getPageShopBonusSalaries({
        id: selectedValue,
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
      }),
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  const handleSelect = e => {
    setSelectedValue(e.target.value);
  };

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedValue(shops[0].id);
      // dispatch(getShopBonusSalaries({ shopId: shops[0].id }));
    }
  }, [shops]);

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort, selectedValue]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sortParam = params.get(SORT);
    if (page && sortParam) {
      const sortSplit = sortParam.split(',');
      setPagination({
        ...pagination,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () =>
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    getUsersFromProps();
  };

  const toggleActive = user => () => {};

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = pagination.sort;
    const order = pagination.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="penalty-page-heading" data-cy="penaltyPageHeading">
        <Translate contentKey="bonusSalary.home.title">보너스</Translate>
        <div className="d-flex justify-content-end">
          <select onChange={handleSelect} value={selectedValue} className="custom-number-input">
            {shops.map(shop => (
              <option value={shop.id} key={shop.id}>
                {shop.nameKo}
              </option>
            ))}
          </select>
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />
            <Translate contentKey="baseSalary.home.refreshListLabel">목록 조회</Translate>
          </Button>
          <Link to={`${selectedValue}/new`} className="btn btn-primary jh-create-entity">
            <FontAwesomeIcon icon="plus" /> <Translate contentKey="bonusSalary.home.createLabel">보너스 생성</Translate>
          </Link>
        </div>
      </h2>
      <Table responsive striped>
        <thead>
          <tr>
            <th className="hand" onClick={sort('id')}>
              <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
            </th>
            <th className="hand" onClick={sort('nameKo')}>
              <Translate contentKey="global.label.nameKo">한글명</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('nameKo')} />
            </th>
            <th className="hand" onClick={sort('nameEn')}>
              <Translate contentKey="global.label.nameEn">영문명</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('nameEn')} />
            </th>
            <th className="hand" onClick={sort('type')}>
              <Translate contentKey="bonusSalary.type">타입</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
            </th>
            <th className="hand" onClick={sort('price')}>
              <Translate contentKey="global.label.price">금액</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
            </th>
            <th>
              <Translate contentKey="userManagement.profiles">Profiles</Translate>
            </th>
            <th className="hand" onClick={sort('createdDate')}>
              <Translate contentKey="userManagement.createdDate">Created Date</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
            </th>
            <th className="hand" onClick={sort('lastModifiedBy')}>
              <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
            </th>
            <th id="modified-date-sort" className="hand" onClick={sort('lastModifiedDate')}>
              <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
            </th>
            <th />
          </tr>
        </thead>
        <tbody>
          {bonusSalariesForPage.length <= 0 && (
            <tr>
              <td colSpan={10} className="no-data-text-align-center">
                <Translate contentKey="global.messages.info.noDataList">조회된 데이터가 없습니다.</Translate>
              </td>
            </tr>
          )}
          {bonusSalariesForPage.map((user, i) => (
            <tr id={user.id} key={`user-${i}`}>
              <td>
                <Button tag={Link} to={`${selectedValue}/${user.id}`} color="link" size="sm">
                  {user.id}
                </Button>
              </td>
              <td>{user.nameKo}</td>
              <td>{user.nameEn}</td>
              <td>{getBonusTypeName(user.type)}</td>
              <td>{user.price}</td>
              <td>
                {user.activated ? (
                  <Button color="success" onClick={toggleActive(user)}>
                    <Translate contentKey="userManagement.activated">Activated</Translate>
                  </Button>
                ) : (
                  <Button color="danger" onClick={toggleActive(user)}>
                    <Translate contentKey="userManagement.deactivated">Deactivated</Translate>
                  </Button>
                )}
              </td>
              <td>
                {user.createdDate ? (
                  <TextFormat value={user.createdDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
                ) : null}
              </td>
              <td>{user.lastModifiedBy}</td>
              <td>
                {user.lastModifiedDate ? (
                  <TextFormat value={user.lastModifiedDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
                ) : null}
              </td>
              <td className="text-end">
                <div className="btn-group flex-btn-group-container">
                  <Button tag={Link} to={`${selectedValue}/${user.id}`} color="info" size="sm">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.view">보기</Translate>
                    </span>
                  </Button>
                  <Button tag={Link} to={`${selectedValue}/${user.id}/edit`} color="primary" size="sm">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.edit">수정</Translate>
                    </span>
                  </Button>
                  {/*
                  <Button tag={Link} to={`${selectedValue}/${user.id}/delete`} color="danger" size="sm">
                    <FontAwesomeIcon icon="trash" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.delete">삭제</Translate>
                    </span>
                  </Button>
                  */}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {totalItems ? (
        <div className={bonusSalariesForPage?.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={pagination.activePage} total={totalItems} itemsPerPage={pagination.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={pagination.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={pagination.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default BonusSalary;
