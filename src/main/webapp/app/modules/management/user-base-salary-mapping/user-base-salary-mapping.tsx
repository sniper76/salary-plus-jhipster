import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getPaginationState, JhiItemCount, JhiPagination, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getBaseSalaryMappingUsers } from './user-base-salary-mapping.reducer';
import { getUserShops } from 'app/modules/order/order.reducer';

import '../management.scss';

export const UserBaseSalaryMapping = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const shops = useAppSelector(state => state.orders.shops);

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedValue, setSelectedValue] = useState(-1);

  const getUsersFromProps = () => {
    dispatch(
      getBaseSalaryMappingUsers({
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

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedValue(shops[0].id);
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

  const users = useAppSelector(state => state.userBaseSalaryMappings.users);
  const totalItems = useAppSelector(state => state.userBaseSalaryMappings.totalItems);
  const loading = useAppSelector(state => state.userBaseSalaryMappings.loading);
  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = pagination.sort;
    const order = pagination.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  const handleSelect = e => {
    setSelectedValue(e.target.value);
  };

  return (
    <div>
      <h2 id="user-management-page-heading" data-cy="userManagementPageHeading">
        <Translate contentKey="userBaseSalaryMapping.home.title">Users</Translate>
        <div className="d-flex justify-content-end">
          <select onChange={handleSelect} value={selectedValue} className="custom-number-input">
            {shops.map(shop => (
              <option value={shop.id} key={shop.id}>
                {shop.nameKo}
              </option>
            ))}
          </select>
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="userBaseSalaryMapping.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${selectedValue}/all_create`} className="btn btn-primary jh-create-entity">
            <FontAwesomeIcon icon="plus" /> <Translate contentKey="userBaseSalaryMapping.home.allMappingLabel">전체 사용자 연결</Translate>
          </Link>
        </div>
      </h2>
      <Table responsive striped>
        <thead>
          <tr>
            <th className="hand" onClick={sort('login')}>
              <Translate contentKey="userBaseSalaryMapping.login">Login</Translate>
              <FontAwesomeIcon icon={getSortIconByFieldName('login')} />
            </th>
            <th className="hand" onClick={sort('firstName')}>
              <Translate contentKey="userBaseSalaryMapping.firstName">FirstName</Translate>
              <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
            </th>
            <th className="hand" onClick={sort('lastName')}>
              <Translate contentKey="userBaseSalaryMapping.lastName">LastName</Translate>
              <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
            </th>
            <th className="hand" onClick={sort('modelNo')}>
              <Translate contentKey="userBaseSalaryMapping.modelNo">ModelNo</Translate>
              <FontAwesomeIcon icon={getSortIconByFieldName('modelNo')} />
            </th>
            <th className="hand" onClick={sort('price')}>
              <Translate contentKey="global.label.price">Price</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
            </th>
            <th className="hand" onClick={sort('createdDate')}>
              <Translate contentKey="userBaseSalaryMapping.createdDate">Created Date</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
            </th>
            <th className="hand" onClick={sort('lastModifiedBy')}>
              <Translate contentKey="userBaseSalaryMapping.lastModifiedBy">Last Modified By</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
            </th>
            <th id="modified-date-sort" className="hand" onClick={sort('lastModifiedDate')}>
              <Translate contentKey="userBaseSalaryMapping.lastModifiedDate">Last Modified Date</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
            </th>
            <th />
          </tr>
        </thead>
        <tbody>
          {users.length <= 0 && (
            <tr>
              <td colSpan={9} className="no-data-text-align-center">
                <Translate contentKey="global.messages.info.noDataList">조회된 데이터가 없습니다.</Translate>
              </td>
            </tr>
          )}
          {users.map((user, i) => (
            <tr id={user.login} key={`user-${i}`}>
              <td>{user.login}</td>
              <td>{user.firstName}</td>
              <td>{user.lastName}</td>
              <td>{user.modelNo}</td>
              <td>{user.price}</td>
              <td>
                {user.createdDate ? <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}
              </td>
              <td>{user.lastModifiedBy}</td>
              <td>
                {user.lastModifiedDate ? (
                  <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                ) : null}
              </td>
              <td className="text-end">
                <div className="btn-group flex-btn-group-container">
                  <Link to={`${selectedValue}/${user.userId}/create`} className="btn btn-primary jh-create-entity">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="userBaseSalaryMapping.home.mappingLabel">사용자 일당 연결하기</Translate>
                    </span>
                  </Link>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {totalItems ? (
        <div className={users?.length > 0 ? '' : 'd-none'}>
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

export default UserBaseSalaryMapping;
