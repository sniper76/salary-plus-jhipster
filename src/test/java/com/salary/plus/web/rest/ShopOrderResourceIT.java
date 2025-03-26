package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shiver.me.timbers.data.random.RandomLongs.somePositiveLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopTable;
import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.repository.ShopTableRepository;
import com.salary.plus.repository.ShopUserMappingRepository;
import com.salary.plus.repository.ShopUserSalesSalaryRepository;
import com.salary.plus.repository.UserRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.service.dto.ShopOrderCreateDTO;
import com.salary.plus.service.dto.ShopOrderDetailResponse;
import com.salary.plus.service.dto.ShopOrderResponse;
import com.salary.plus.utils.DateTimeFormatUtil;
import com.salary.plus.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class ShopOrderResourceIT {

    private static final String TARGET_URL = "/api/shops/{shopId}/orders/{date}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopTableRepository shopTableRepository;

    @Autowired
    private ShopSalesItemRepository shopSalesItemRepository;

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @Autowired
    private ShopOrderDetailRepository shopOrderDetailRepository;

    @Autowired
    private ShopUserSalesSalaryRepository shopUserSalesSalaryRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    @Nested
    class WhenSuccess {

        private Long shopId;
        private String date;
        private Long orderId;
        private Long tableId;
        private List<Long> salesItemIds;
        private List<Long> modelIds;
        private List<Integer> prices;
        private List<Long> orderDetailIds;

        private Integer longTimeBarFinePrice = 4000;
        private Integer ladyDrinkPrice = 350;
        private Integer guestDrinkPrice = 150;
        private Integer totalPrice = longTimeBarFinePrice + ladyDrinkPrice + guestDrinkPrice;
        private Integer snackPrice = 10000;

        private Long userId1;
        private Long userId2;

        private void mockData() {
            Shop shop = new Shop();
            shop.setType(ShopType.BAR);
            shop.setNameKo("shop1");
            shop.setNameEn("shop1");
            shopRepository.saveAndFlush(shop);

            shopId = shop.getId();

            ShopTable shopTable = new ShopTable();
            shopTable.setShopId(shopId);
            shopTable.setNo("T01");
            shopTableRepository.saveAndFlush(shopTable);

            tableId = shopTable.getId();

            ShopSalesItem shopSalesItem1 = new ShopSalesItem();
            shopSalesItem1.setShopId(shopId);
            shopSalesItem1.setNameKo("item1 ko");
            shopSalesItem1.setNameEn("item1 en");
            shopSalesItem1.setPrice(longTimeBarFinePrice);
            shopSalesItem1.setCommissionTargetYn(true);
            shopSalesItem1.setShopCommissionPrice(0);
            shopSalesItem1.setMamaCommissionPrice(0);
            shopSalesItem1.setModelCommissionPrice(0);
            shopSalesItem1.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem1);

            ShopSalesItem shopSalesItem2 = new ShopSalesItem();
            shopSalesItem2.setShopId(shopId);
            shopSalesItem2.setNameKo("item2 ko");
            shopSalesItem2.setNameEn("item2 en");
            shopSalesItem2.setPrice(ladyDrinkPrice);
            shopSalesItem2.setCommissionTargetYn(true);
            shopSalesItem2.setShopCommissionPrice(0);
            shopSalesItem2.setMamaCommissionPrice(0);
            shopSalesItem2.setModelCommissionPrice(0);
            shopSalesItem2.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem2);

            ShopSalesItem shopSalesItem3 = new ShopSalesItem();
            shopSalesItem3.setShopId(shopId);
            shopSalesItem3.setNameKo("item3 ko");
            shopSalesItem3.setNameEn("item3 en");
            shopSalesItem3.setPrice(guestDrinkPrice);
            shopSalesItem3.setCommissionTargetYn(false);
            shopSalesItem3.setShopCommissionPrice(0);
            shopSalesItem3.setMamaCommissionPrice(0);
            shopSalesItem3.setModelCommissionPrice(0);
            shopSalesItem3.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem3);

            date = DateUtils.getFormatted(DateTimeFormatUtil.yyyy_MM_dd());
            salesItemIds = new ArrayList<>();
            salesItemIds.add(shopSalesItem1.getId());
            salesItemIds.add(shopSalesItem2.getId());
            salesItemIds.add(shopSalesItem3.getId());

            prices = new ArrayList<>();
            prices.add(shopSalesItem1.getPrice());
            prices.add(shopSalesItem2.getPrice());
            prices.add(shopSalesItem3.getPrice());

            userId1 = somePositiveLong();
            userId2 = somePositiveLong();
            modelIds = new ArrayList<>();
            modelIds.add(userId1);
            modelIds.add(userId2);
        }

        private void mockOrderData() {
            ShopOrder shopOrder = new ShopOrder();
            shopOrder.setShopId(shopId);
            shopOrder.setShopTableId(tableId);
            shopOrder.setPaid(false);
            shopOrder.setDate(date);
            shopOrder.setTotalPrice(totalPrice);
            shopOrderRepository.saveAndFlush(shopOrder);

            orderId = shopOrder.getId();

            ShopOrderDetail shopOrderDetail1 = new ShopOrderDetail();
            shopOrderDetail1.setShopOrderId(shopOrder.getId());
            shopOrderDetail1.setShopSalesItemId(salesItemIds.get(0));
            shopOrderDetail1.setPrice(longTimeBarFinePrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail1);

            ShopOrderDetail shopOrderDetail2 = new ShopOrderDetail();
            shopOrderDetail2.setShopOrderId(shopOrder.getId());
            shopOrderDetail2.setShopSalesItemId(salesItemIds.get(1));
            shopOrderDetail2.setPrice(ladyDrinkPrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail2);

            ShopOrderDetail shopOrderDetail3 = new ShopOrderDetail();
            shopOrderDetail3.setShopOrderId(shopOrder.getId());
            shopOrderDetail3.setShopSalesItemId(salesItemIds.get(2));
            shopOrderDetail3.setPrice(guestDrinkPrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail3);

            orderDetailIds = new ArrayList<>();
            orderDetailIds.add(shopOrderDetail1.getId());
            orderDetailIds.add(shopOrderDetail2.getId());
            orderDetailIds.add(shopOrderDetail3.getId());

            ShopUserSalesSalary shopUserSalesSalary1 = new ShopUserSalesSalary();
            shopUserSalesSalary1.setShopOrderDetailId(shopOrderDetail1.getId());
            shopUserSalesSalary1.setUserId(userId1);
            shopUserSalesSalary1.setPrice(longTimeBarFinePrice);
            shopUserSalesSalaryRepository.saveAndFlush(shopUserSalesSalary1);

            ShopUserSalesSalary shopUserSalesSalary2 = new ShopUserSalesSalary();
            shopUserSalesSalary2.setShopOrderDetailId(shopOrderDetail2.getId());
            shopUserSalesSalary2.setUserId(userId2);
            shopUserSalesSalary2.setPrice(ladyDrinkPrice);
            shopUserSalesSalaryRepository.saveAndFlush(shopUserSalesSalary2);
        }

        @DisplayName("수정인 경우")
        @Nested
        class WhenUpdate {

            @DisplayName("변경된 정보가 없는 경우")
            @Nested
            class WhenNotChangeData {

                @BeforeEach
                void setUp() {
                    mockData();
                    mockOrderData();
                }

                @DisplayName("정상 등록된다.")
                @Test
                @Transactional
                void create() throws Exception {
                    // Create the User
                    ShopOrderCreateDTO createDTO = new ShopOrderCreateDTO();
                    createDTO.setOrderId(orderId);
                    createDTO.setTableId(tableId);
                    createDTO.setSalesItemIds(salesItemIds);
                    createDTO.setPrices(prices);
                    createDTO.setModelIds(modelIds);
                    createDTO.setOrderDetailIds(orderDetailIds);

                    restUserMockMvc
                        .perform(
                            patch(TARGET_URL, shopId, date).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(createDTO))
                        )
                        .andExpect(status().isCreated());

                    final List<ShopOrderDetailResponse> allOrderDetailResponse = shopOrderRepository.findAllByIdAndShopIdAndDate(
                        orderId,
                        shopId,
                        date,
                        true
                    );
                    assertThat(allOrderDetailResponse.size()).isEqualTo(3);
                }
            }

            @DisplayName("안주 정보가 추가된 경우")
            @Nested
            class WhenAddSnackData {

                @BeforeEach
                void setUp() {
                    mockData();
                    mockOrderData();

                    ShopSalesItem shopSalesItem4 = new ShopSalesItem();
                    shopSalesItem4.setShopId(shopId);
                    shopSalesItem4.setNameKo("item3 ko");
                    shopSalesItem4.setNameEn("item3 en");
                    shopSalesItem4.setPrice(snackPrice);
                    shopSalesItem4.setCommissionTargetYn(false);
                    shopSalesItem4.setShopCommissionPrice(0);
                    shopSalesItem4.setMamaCommissionPrice(0);
                    shopSalesItem4.setModelCommissionPrice(0);
                    shopSalesItem4.setSnackYn(true);
                    shopSalesItemRepository.saveAndFlush(shopSalesItem4);

                    salesItemIds.add(shopSalesItem4.getId());
                    prices.add(shopSalesItem4.getPrice());
                }

                @DisplayName("정상 등록된다.")
                @Test
                @Transactional
                void create() throws Exception {
                    // Create the User
                    ShopOrderCreateDTO createDTO = new ShopOrderCreateDTO();
                    createDTO.setOrderId(orderId);
                    createDTO.setTableId(tableId);
                    createDTO.setSalesItemIds(salesItemIds);
                    createDTO.setPrices(prices);
                    createDTO.setModelIds(modelIds);
                    createDTO.setOrderDetailIds(orderDetailIds);

                    restUserMockMvc
                        .perform(
                            patch(TARGET_URL, shopId, date).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(createDTO))
                        )
                        .andExpect(status().isCreated());

                    final ShopOrder shopOrderResponse = shopOrderRepository.findByIdAndShopIdAndDate(orderId, shopId, date).orElseThrow();
                    assertThat(shopOrderResponse.getTotalPrice()).isEqualTo(totalPrice + snackPrice);
                    final List<ShopOrderDetailResponse> allOrderDetailResponse = shopOrderRepository.findAllByIdAndShopIdAndDate(
                        orderId,
                        shopId,
                        date,
                        true
                    );
                    assertThat(allOrderDetailResponse.size()).isEqualTo(4);
                    final ShopOrderDetailResponse shopOrderDetailResponse = allOrderDetailResponse.get(3);
                    assertThat(shopOrderDetailResponse.isSnackYn()).isEqualTo(true);
                    assertThat(shopOrderDetailResponse.getPrice()).isEqualTo(snackPrice);
                }
            }

            @DisplayName("안주 정보가 추가되고 롱타임 바파인 모델이 변경된 경우")
            @Nested
            class WhenAddSnackDataAndChangeBarFineModel {

                private Long userId3;

                @BeforeEach
                void setUp() {
                    mockData();
                    mockOrderData();

                    userId3 = somePositiveLong();
                    modelIds.set(0, userId3);

                    ShopSalesItem shopSalesItem4 = new ShopSalesItem();
                    shopSalesItem4.setShopId(shopId);
                    shopSalesItem4.setNameKo("item3 ko");
                    shopSalesItem4.setNameEn("item3 en");
                    shopSalesItem4.setPrice(snackPrice);
                    shopSalesItem4.setCommissionTargetYn(false);
                    shopSalesItem4.setShopCommissionPrice(0);
                    shopSalesItem4.setMamaCommissionPrice(0);
                    shopSalesItem4.setModelCommissionPrice(0);
                    shopSalesItem4.setSnackYn(true);
                    shopSalesItemRepository.saveAndFlush(shopSalesItem4);

                    salesItemIds.add(shopSalesItem4.getId());
                    prices.add(shopSalesItem4.getPrice());
                }

                @DisplayName("정상 등록된다.")
                @Test
                @Transactional
                void create() throws Exception {
                    // Create the User
                    ShopOrderCreateDTO createDTO = new ShopOrderCreateDTO();
                    createDTO.setOrderId(orderId);
                    createDTO.setTableId(tableId);
                    createDTO.setSalesItemIds(salesItemIds);
                    createDTO.setPrices(prices);
                    createDTO.setModelIds(modelIds);
                    createDTO.setOrderDetailIds(orderDetailIds);

                    restUserMockMvc
                        .perform(
                            patch(TARGET_URL, shopId, date).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(createDTO))
                        )
                        .andExpect(status().isCreated());

                    final ShopOrder shopOrderResponse = shopOrderRepository.findByIdAndShopIdAndDate(orderId, shopId, date).orElseThrow();
                    assertThat(shopOrderResponse.getTotalPrice()).isEqualTo(totalPrice + snackPrice);
                    final List<ShopOrderDetailResponse> allOrderDetailResponse = shopOrderRepository.findAllByIdAndShopIdAndDate(
                        orderId,
                        shopId,
                        date,
                        true
                    );
                    assertThat(allOrderDetailResponse.size()).isEqualTo(4);
                    final ShopOrderDetailResponse shopOrderDetailResponse3 = allOrderDetailResponse.get(3);
                    assertThat(shopOrderDetailResponse3.isSnackYn()).isEqualTo(true);
                    assertThat(shopOrderDetailResponse3.getPrice()).isEqualTo(snackPrice);
                    final ShopOrderDetailResponse shopOrderDetailResponse0 = allOrderDetailResponse.get(0);
                    assertThat(shopOrderDetailResponse0.getModelId()).isEqualTo(userId3);
                }
            }
        }

        @DisplayName("등록인 경우")
        @Nested
        class WhenCreate {

            @BeforeEach
            void setUp() {
                mockData();
            }

            @DisplayName("정상 등록된다.")
            @Test
            @Transactional
            void create() throws Exception {
                // Create the User
                ShopOrderCreateDTO createDTO = new ShopOrderCreateDTO();
                createDTO.setTableId(tableId);
                createDTO.setSalesItemIds(salesItemIds);
                createDTO.setPrices(prices);
                createDTO.setModelIds(modelIds);

                restUserMockMvc
                    .perform(
                        post(TARGET_URL, shopId, date).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(createDTO))
                    )
                    .andExpect(status().isCreated());

                final List<ShopOrderResponse> allOrderResponse = shopOrderRepository.findAllByShopIdAndDateAndActivated(shopId, date, true);
                assertThat(allOrderResponse.size()).isEqualTo(1);
                final ShopOrderResponse orderResponse = allOrderResponse.get(0);
                final List<ShopOrderDetailResponse> allOrderDetailResponse = shopOrderRepository.findAllByIdAndShopIdAndDate(
                    orderResponse.getOrderId(),
                    shopId,
                    date,
                    true
                );
                assertThat(allOrderDetailResponse.size()).isEqualTo(3);
            }
        }
    }
}
