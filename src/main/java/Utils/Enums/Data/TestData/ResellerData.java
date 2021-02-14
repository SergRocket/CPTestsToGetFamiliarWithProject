package Utils.Enums.Data.TestData;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResellerData extends RoleData {
    private boolean isApproved;
    private String availableFunds;
    private String availableBalance;

    public ResellerData expandWith(ResellerData resellerData) {
        return ObjectUtils.expandObjectProperties(this, resellerData);
    }

    public List<UserData> getUsers() {
        if (users != null)
            users = users.stream()
                    .peek(usersData -> usersData.setResellerData(this))
                    .collect(Collectors.toList());
        return users;
    }

    public void setApproved(String approved) {
        isApproved = approved.toLowerCase().equals("yes");
    }

    public ResellerData withSPOCUser(UserData spocUsersData) {
        setEmail(spocUsersData.getEmail());
        setPassword(spocUsersData.getPassword());
        return this;
    }

    public List<AccountData> getAccounts() {
        if (!users.isEmpty()) {
            return users.stream()
                    .peek(userData -> userData.setResellerData(this))
                    .flatMap(userData -> userData.getAccounts().stream())
                    .collect(Collectors.toList());
        }
        throw new NoSuchElementException("Reseller doesn't contains users with accounts");
    }

    public boolean isApproved() {
        return isApproved;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResellerData that = (ResellerData) o;
        return Objects.equals(name, that.getName()) && Objects.equals(email, that.getEmail()) && Objects.equals(partnerUrl, that.getPartnerUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, partnerUrl);
    }

    @Override
    public String toString() {
        return ObjectUtils.createToString(name, email, partnerUrl);
    }

    public String getAvailableFunds() {
        return availableFunds;
    }



}
