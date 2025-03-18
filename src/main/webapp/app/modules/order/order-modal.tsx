import React, { useState, useEffect } from 'react';
import { Translate, ValidatedField } from 'react-jhipster';
import { Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { useForm } from 'react-hook-form';
import { languages, locales } from 'app/config/translation';
import './order.scss';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';
import { IModelUser } from 'app/shared/model/modelUser.model';
import { IShopTable } from 'app/shared/model/shopTable.model';
import { useAppDispatch } from 'app/config/store';
import { deleteOrderDetail } from 'app/modules/order/order.reducer';

export interface IOrderModalProps {
  showModal: boolean;
  orderError: boolean;
  handleOrder: (obj: any) => void;
  handleClose: () => void;
  salesItems: ReadonlyArray<IShopSalesItem>;
  models: ReadonlyArray<IModelUser>;
  tables: ReadonlyArray<IShopTable>;
  orderDetails: [];
  orderId: null;
  tableId: null;
}

const OrderModal = (props: IOrderModalProps) => {
  const orderHandleProps = obj => {
    props.handleOrder(obj);
  };

  const {
    handleSubmit,
    register,
    reset,
    watch,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const dispatch = useAppDispatch();

  const { orderError, handleClose } = props;

  const [leftItems, setLeftItems] = useState([]); // 왼쪽 div 아이템들
  const [draggedItem, setDraggedItem] = useState(null); // 드래그 중인 아이템

  const handleOrderSubmit = e => {
    // console.warn('handleOrderSubmit', e, leftItems);
    handleSubmit(orderHandleProps)(e);
    handleClose();
  };

  const handleDragStart = item => {
    // console.warn('handleDragStart', item);
    setDraggedItem(item); // 드래그 시작 시 아이템 저장
  };

  const handleDragOver = e => {
    e.preventDefault(); // 기본 동작 방지 (drop 이벤트 허용)
  };

  const handleDrop = () => {
    if (draggedItem) {
      reset();
      setLeftItems(prev => [...prev, draggedItem]); // 왼쪽에 복사 추가
    }
  };

  const handleRemove = (index, item) => {
    setLeftItems(prev => prev.filter((_, i) => i !== index)); // 왼쪽에서 삭제
    // console.warn('handleRemove', item);
    if (item.salesItemId) {
      dispatch(deleteOrderDetail({ orderId: props.orderId, orderDetailId: item.id }));
    }
  };

  // useEffect(() => {
  //   if (props.orderDetails) {
  //     setLeftItems(props.orderDetails);
  //   }
  // }, [props]);
  // useEffect(() => {
  //   if (props.orderId && props.orderDetails && props.orderDetails.length > 0) {
  //     // props.orderDetails 가 변경될 때마다 leftItems 를 업데이트
  //     console.warn('OrderModal useEffect orderDetails', props.orderDetails);
  //     // setLeftItems(props.orderDetails);
  //     setLeftItems([...props.orderDetails]); // orderDetails 를 복사하여 설정
  //     console.warn('OrderModal useEffect leftItems', leftItems);
  //     // setLeftItems((props.orderDetails as any[]).map(prev => [...prev]));
  //     // setLeftItems((props.orderDetails as any[]).map(item => ({ ...item })));
  //   } else {
  //     // 새로운 주문 생성 시 초기화
  //     setLeftItems([]);
  //   }
  // }, [props.orderDetails, props.orderId]);
  useEffect(() => {
    reset(); // react-hook-form 의 상태도 초기화
    if (props.orderId && props.orderDetails && props.orderDetails.length > 0) {
      // console.warn('OrderModal: 기존 주문 불러오기', props.orderDetails);
      setLeftItems([...props.orderDetails]); // 기존 주문 정보로 leftItems 설정
    } else if (!props.orderId) {
      // console.warn('OrderModal: 새로운 주문 초기화');
      setLeftItems([]); // 새로운 주문일 경우 leftItems 초기화
    }
  }, [props.orderDetails, props.orderId, reset]);

  // leftItems 값이 실제로 업데이트된 이후 확인
  useEffect(() => {
    // console.warn('OrderModal leftItems Updated', leftItems);
  }, [props]);

  // watch() 는 폼의 모든 값을 실시간으로 관찰합니다.
  const allValues = watch();

  useEffect(() => {
    // console.warn('Current Form Values:', allValues); // 값이 변경될 때마다 출력
  }, [allValues]);

  return (
    <Modal isOpen={props.showModal} toggle={handleClose} backdrop="static" id="order-page" autoFocus={false}>
      <Form onSubmit={handleOrderSubmit}>
        <ModalHeader id="order-title" data-cy="orderTitle" toggle={handleClose}>
          <Translate contentKey="global.menu.order">Order</Translate>
        </ModalHeader>
        <ModalBody>
          <Row>
            <Col md="12">
              <div className="container">
                {props.orderId && (
                  <ValidatedField type="hidden" register={register} id="orderId" name="orderId" data-cy="orderId" value={props.orderId} />
                )}
                <ValidatedField
                  className="select-box"
                  type="select"
                  register={register}
                  id="tableId"
                  name="tableId"
                  data-cy="tableId"
                  defaultValue={props.tableId}
                >
                  {props.tables.map((model, idx) => (
                    <option value={model.id} key={idx}>
                      {model.no}
                    </option>
                  ))}
                </ValidatedField>
              </div>
              <div className="container">
                {/* 왼쪽 Div (드롭 가능) */}
                <div id="left" className="box" onDragOver={handleDragOver} onDrop={handleDrop}>
                  {leftItems.map((item, index) => {
                    // console.warn('Rendering item:', item, leftItems); // 콘솔 출력
                    return (
                      <div key={index} className="item">
                        <div className="item-header">
                          <div className="top">
                            {item.nameKo}
                            <ValidatedField
                              type="hidden"
                              register={register}
                              id={`salesItemIds[${index}]`}
                              name={`salesItemIds[${index}]`}
                              data-cy={`salesItemIds[${index}]`}
                              value={item.id}
                            />
                            <ValidatedField
                              type="hidden"
                              register={register}
                              id={`orderDetailIds[${index}]`}
                              name={`orderDetailIds[${index}]`}
                              data-cy={`orderDetailIds[${index}]`}
                              value={item.orderDetailId}
                            />
                            <button className="close-btn" onClick={() => handleRemove(index, item)}>
                              ✖
                            </button>
                          </div>
                          <div className="bottom">
                            {item.commissionTarget && (
                              <ValidatedField
                                className="select-box"
                                register={register}
                                type="select"
                                id={`modelIds[${index}]`}
                                name={`modelIds[${index}]`}
                                data-cy={`modelIds[${index}]`}
                                defaultValue={item.modelId}
                              >
                                {props.models.map((model, idx) => (
                                  <option value={model.id} key={idx}>
                                    {model.modelNo}
                                  </option>
                                ))}
                              </ValidatedField>
                            )}
                            {item.snack ? (
                              <ValidatedField
                                register={register}
                                type="number"
                                id={`prices[${index}]`}
                                name={`prices[${index}]`}
                                validate={{
                                  required: { value: true, message: 'Price is required.' },
                                  min: { value: 0, message: 'Price must be a positive number.' },
                                }}
                                data-cy={`prices[${index}]`}
                                defaultValue={item.price || 0}
                              />
                            ) : (
                              <ValidatedField
                                register={register}
                                type="hidden"
                                id={`prices[${index}]`}
                                name={`prices[${index}]`}
                                data-cy={`prices[${index}]`}
                                value={item.price || 0}
                              />
                            )}
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>

                {/* 오른쪽 Div (드래그 가능) */}
                <div id="right" className="box">
                  {props.salesItems.map((item, index) => (
                    <div key={index} className="item" draggable onDragStart={() => handleDragStart(item)}>
                      {item.nameKo}
                    </div>
                  ))}
                </div>
              </div>
            </Col>
          </Row>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handleClose} tabIndex={1}>
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>{' '}
          <Button color="primary" type="submit" data-cy="submit">
            <Translate contentKey="order.form.button">Ordering</Translate>
          </Button>
        </ModalFooter>
      </Form>
    </Modal>
  );
};

export default OrderModal;
